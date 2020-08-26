locals {
  secondary_domain_name = "kodemaker.no"

  domain_name = "kodemaker.no"
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
  comment = "Origin access identity for www.${local.domain_name}"
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
  name_prefix = "www.${local.domain_name}"
  assume_role_policy = "${data.aws_iam_policy_document.lambda.json}"
}

resource "aws_iam_role_policy_attachment" "basic" {
  role = "${aws_iam_role.lambda_role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

data "archive_file" "viewer_request" {
  type = "zip"
  output_path = "${path.module}/.zip/rewrite.zip"

  source {
    filename = "viewer-request.js"
    content = "${file("${path.module}/viewer-request.js")}"
  }
}

resource "aws_lambda_function" "viewer_request" {
  provider = "aws.us-east-1"
  function_name = "kodemaker-url-rewriter"
  filename = "${data.archive_file.viewer_request.output_path}"
  source_code_hash = "${data.archive_file.viewer_request.output_base64sha256}"
  role = "${aws_iam_role.lambda_role.arn}"
  runtime = "nodejs12.x"
  handler = "viewer-request.handler"
  memory_size = 128
  timeout = 3
  publish = true
}

data "archive_file" "viewer_response" {
  type = "zip"
  output_path = "${path.module}/.zip/headers.zip"

  source {
    filename = "viewer-response.js"
    content = "${file("${path.module}/viewer-response.js")}"
  }
}

resource "aws_lambda_function" "viewer_response" {
  provider = "aws.us-east-1"
  function_name = "kodemaker-security-headers"
  filename = "${data.archive_file.viewer_response.output_path}"
  source_code_hash = "${data.archive_file.viewer_response.output_base64sha256}"
  role = "${aws_iam_role.lambda_role.arn}"
  runtime = "nodejs12.x"
  handler = "viewer-response.handler"
  memory_size = 128
  timeout = 3
  publish = true
}

resource "aws_acm_certificate" "cert" {
  provider = "aws.us-east-1"
  domain_name = "${local.domain_name}"
  subject_alternative_names = ["www.${local.domain_name}"]
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_cloudfront_distribution" "s3_distribution" {
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
  comment = "Distribution for www.${local.domain_name}"
  default_root_object = "index.html"
  aliases = [
    "${local.domain_name}",
    "www.${local.domain_name}"
  ]

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

    min_ttl = 0
    default_ttl = 86400
    max_ttl = 604800
    compress = true
    viewer_protocol_policy = "redirect-to-https"

    lambda_function_association {
      event_type = "viewer-request"
      lambda_arn = "${aws_lambda_function.viewer_request.qualified_arn}"
      include_body = false
    }

    lambda_function_association {
      event_type = "viewer-response"
      lambda_arn = "${aws_lambda_function.viewer_response.qualified_arn}"
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
    viewer_protocol_policy = "redirect-to-https"

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

resource "aws_route53_record" "root_record" {
  name = "www.${local.domain_name}"
  zone_id = "${data.aws_route53_zone.zone.zone_id}"
  type = "A"

  alias {
    name = "${aws_cloudfront_distribution.s3_distribution.domain_name}"
    zone_id = "${aws_cloudfront_distribution.s3_distribution.hosted_zone_id}"
    evaluate_target_health = true
  }
}
