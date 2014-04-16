/*global Buffer */

var https = require('https');
var querystring = require("querystring");
var config = require("./mail-config");

var auth = 'Basic ' + new Buffer(config.apiUser).toString('base64');

module.exports = function (params) {
  var data = querystring.stringify(params);

  var options = {
    hostname: "api.mailgun.net",
    port: 443,
    path: "/v2/samples.mailgun.org/messages",
    method: "POST",
    headers: {
      "Authorization": auth,
      "Content-Type": "application/x-www-form-urlencoded",
      "Content-Length": data.length
    }
  };

  var req = https.request(options, function (res) {
    var out = "";
    res.setEncoding('utf8');
    res.on("data", function (chunk) {
      out = out + chunk;
    });
    res.on("end", function () {
      console.log(out);
      console.log("");
    });
  });
  req.write(data);
  req.end();
};
