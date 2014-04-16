var sys = require('sys')
var exec = require('child_process').exec;
var http = require('http');
var qs = require("querystring");
var sendMail = require("./send-mail");
var config = require("./mail-config");

http.createServer(function (req, res) {
  if (req.method == 'POST') {
    var body = '';
    req.on('data', function (data) {
      body += data;
      if (body.length > 1e5) {
        res.writeHead(413);
        res.end("Request Entity Too Large\n");
      }
    });
    req.on('end', function () {
      var params = qs.parse(body);

      if (params.tekst || params.omfang || params.oppstart || params.kontakt) {

        var info = [config.subject + ":"];
        if (params.tekst) { info.push(params.tekst); }
        if (params.omfang) { info.push("Omfang: " + params.omfang);}
        if (params.oppstart) { info.push("Oppstart: " + params.oppstart);}
        if (params.kontakt) { info.push("Kontakt: " + params.kontakt);}

        sendMail({
          from: config.from,
          to: config.to,
          subject: config.subject,
          text: info.join("\n\n")
        });

        res.writeHead(302, {'Location': '/takk/'});
        res.end();
      } else {
        res.writeHead(302, {'Location': '/skjema/'});
      }

    });
  } else {
    res.writeHead(404);
    res.end("Not Found\n");
  }
}).listen(8003, '127.0.0.1');

console.log('Mail sender running at http://127.0.0.1:8003/');
