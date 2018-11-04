locals {
  domain_name = "www.kodemaker.no"
  secondary_domain_name = "kodemaker.no"
  s3_origin_id = "StaticFilesS3BucketOrigin"
}

data "aws_route53_zone" "zone" {
  name = "kodemaker.no."
}

resource "aws_s3_bucket" "bucket" {
  bucket = "kodemaker-www"
  acl = "private"

  website {
    index_document = "index.html"
    error_document = "404/index.html"
  }
}

resource "aws_cloudfront_origin_access_identity" "identity" {
  comment = "Origin access identity for ${local.domain_name}"
}

data "aws_iam_policy_document" "s3_policy" {
  statement {
    actions = ["s3:GetObject"]
    resources = ["${aws_s3_bucket.bucket.arn}/*"]

    principals {
      type = "AWS"
      identifiers = ["${aws_cloudfront_origin_access_identity.identity.iam_arn}"]
    }
  }

  statement {
    actions = ["s3:ListBucket"]
    resources = ["${aws_s3_bucket.bucket.arn}"]

    principals {
      type = "AWS"
      identifiers = ["${aws_cloudfront_origin_access_identity.identity.iam_arn}"]
    }
  }
}

resource "aws_s3_bucket_policy" "bucket_policy" {
  bucket = "${aws_s3_bucket.bucket.id}"
  policy = "${data.aws_iam_policy_document.s3_policy.json}"
}

data "aws_iam_policy_document" "lambda" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type = "Service"
      identifiers = [
        "lambda.amazonaws.com",
        "edgelambda.amazonaws.com"
      ]
    }
  }
}

resource "aws_iam_role" "lambda_role" {
  name_prefix = "${local.domain_name}"
  assume_role_policy = "${data.aws_iam_policy_document.lambda.json}"
}

resource "aws_iam_role_policy_attachment" "basic" {
  role = "${aws_iam_role.lambda_role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

data "archive_file" "rewrite" {
  type = "zip"
  output_path = "${path.module}/.zip/rewrite.zip"

  source {
    filename = "lambda.js"
    content = "${file("${path.module}/lambda.js")}"
  }
}

resource "aws_lambda_function" "basic_auth" {
  provider = "aws.us-east-1"
  function_name = "kodemaker-www-url-rewrite"
  filename = "${data.archive_file.rewrite.output_path}"
  source_code_hash = "${data.archive_file.rewrite.output_base64sha256}"
  role = "${aws_iam_role.lambda_role.arn}"
  runtime = "nodejs8.10"
  handler = "lambda.handler"
  memory_size = 128
  timeout = 3
  publish = true
}

resource "aws_acm_certificate" "cert" {
  provider = "aws.us-east-1"
  domain_name = "www.kodemaker.no"
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_route53_record" "cert_validation" {
  name = "${aws_acm_certificate.cert.domain_validation_options.0.resource_record_name}"
  type = "${aws_acm_certificate.cert.domain_validation_options.0.resource_record_type}"
  zone_id = "${data.aws_route53_zone.zone.id}"
  records = ["${aws_acm_certificate.cert.domain_validation_options.0.resource_record_value}"]
  ttl = 60
}

resource "aws_acm_certificate_validation" "cert" {
  provider = "aws.us-east-1"
  certificate_arn = "${aws_acm_certificate.cert.arn}"
  validation_record_fqdns = ["${aws_route53_record.cert_validation.fqdn}"]
}

resource "aws_cloudfront_distribution" "s3_distribution" {
  depends_on = ["aws_acm_certificate_validation.cert"]

  origin {
    domain_name = "${aws_s3_bucket.bucket.bucket_regional_domain_name}"
    origin_id = "${local.s3_origin_id}"

    s3_origin_config {
      origin_access_identity = "${aws_cloudfront_origin_access_identity.identity.cloudfront_access_identity_path}"
    }
  }

  custom_error_response {
    error_code = 404
    response_page_path = "/404/index.html"
    response_code = 404
  }

  enabled = true
  is_ipv6_enabled = true
  comment = "Distribution for ${local.domain_name}"
  default_root_object = "index.html"
  aliases = ["${local.domain_name}"]

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "${local.s3_origin_id}"

    forwarded_values {
      query_string = false

      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "allow-all"
    min_ttl = 0
    default_ttl = 86400
    max_ttl = 604800
    compress = true
    viewer_protocol_policy = "allow-all"

    lambda_function_association {
      event_type = "viewer-request"
      lambda_arn = "${aws_lambda_function.basic_auth.qualified_arn}"
      include_body = false
    }
  }

  # Cache immutable paths for a long time
  ordered_cache_behavior {
    path_pattern = "/assets/*"
    allowed_methods = ["GET", "HEAD", "OPTIONS"]
    cached_methods = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = "${local.s3_origin_id}"
    min_ttl = 0
    default_ttl = 86400
    max_ttl = 31536000
    compress = true
    viewer_protocol_policy = "allow-all"

    forwarded_values {
      query_string = false
      headers = ["Origin"]
      cookies {
        forward = "none"
      }
    }
  }

  price_class = "PriceClass_100"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    acm_certificate_arn = "${aws_acm_certificate.cert.arn}"
    minimum_protocol_version = "TLSv1"
    ssl_support_method = "sni-only"
  }
}

resource "aws_route53_record" "www_record" {
  name = "${local.domain_name}"
  zone_id = "${data.aws_route53_zone.zone.zone_id}"
  type = "A"

  alias {
    name = "${aws_cloudfront_distribution.s3_distribution.domain_name}"
    zone_id = "${aws_cloudfront_distribution.s3_distribution.hosted_zone_id}"
    evaluate_target_health = true
  }
}

resource "aws_s3_bucket" "redirect" {
  bucket = "${local.secondary_domain_name}"
  acl = "public-read"

  website {
    redirect_all_requests_to = "https://${local.domain_name}"
  }
}

resource "aws_route53_record" "root_record" {
  name = "${local.secondary_domain_name}"
  zone_id = "${data.aws_route53_zone.zone.zone_id}"
  type = "A"

  alias {
    name = "${aws_s3_bucket.redirect.website_domain}"
    zone_id = "${aws_s3_bucket.redirect.hosted_zone_id}"
    evaluate_target_health = true
  }
}
