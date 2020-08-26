'use strict';

exports.handler = (event, context, callback) => {
  const response = event.Records[0].cf.response;

  response.headers = Object.assign(response.headers, {
    "Strict-Transport-Security": [{
      key: "Strict-Transport-Security",
      value: "max-age=31536000"
    }],
    "X-Frame-Options": [{
      key: "X-Frame-Options",
      value: "deny"
    }],
    "X-Content-Type-Options": [{
      key: "X-Content-Type-Options",
      value: "nosniff"
    }],
    "Referrer-Policy": [{
      key: "Referrer-Policy",
      value: "strict-origin"
    }]
  });

  callback(null, response);
};
