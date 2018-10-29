locals {
  domain_name = "www.kodemaker.no"
  s3_origin_id = "StaticFilesS3BucketOrigin"
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

resource "aws_cloudfront_distribution" "s3_distribution" {
  origin {
    domain_name = "${aws_s3_bucket.bucket.bucket_regional_domain_name}"
    origin_id = "${local.s3_origin_id}"

    s3_origin_config {
      origin_access_identity = "${aws_cloudfront_origin_access_identity.identity.cloudfront_access_identity_path}"
    }
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
    default_ttl = 3600
    max_ttl = 86400
    compress = true
    viewer_protocol_policy = "allow-all"
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
    cloudfront_default_certificate = "true"
  }
}
