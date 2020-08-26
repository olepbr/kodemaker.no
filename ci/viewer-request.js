'use strict';

function host(request) {
  try {
    return request.headers.host[0].value || '';
  } catch (e) {
    return '';
  }
}

exports.handler = (event, context, callback) => {
  const request = event.Records[0].cf.request;

  if (host(request) === 'kodemaker.no') {
    return callback(null, {
      status: 301,
      headers: {
        location: [{
          key: 'location',
          value: `https://www.kodemaker.no${request.uri}`
        }]
      }
    });
  }

  if (!/\..+/.test(request.uri)) {
    request.uri = `${request.uri.replace(/\/$/, '')}/index.html`;
  }

  callback(null, request);
};
