'use strict';

exports.handler = (event, context, callback) => {
  const host = request.headers.host && request.headers.host[0] && request.headers.host[0].value;

  if (host !== "www.kodemaker.no" && !/cloudfront.net$/.test(host)) {
    return callback(null, {
      status: 302,
      headers: {location: `https://www.kodemaker.no${request.uri}`}
    });
  }

  const request = event.Records[0].cf.request;

  if (!/\..+/.test(request.uri)) {
    request.uri = `${request.uri.replace(/\/$/, '')}/index.html`;
  }

  callback(null, request);
};
