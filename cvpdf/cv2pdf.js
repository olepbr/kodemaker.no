const http = require('http');
const fs = require('fs');
const path = require('path');
const puppeteer = require('puppeteer');
const finalhandler = require('finalhandler');
const serveStatic = require('serve-static');

async function html2pdf(page, url, file) {
  await page.goto(url);
  await page.emulateMediaType('screen');

  await page.$eval('body', element => element.className = 'pdf');

  await page.pdf({
    path: file,
    scale: 0.75
  });
}

const port = 8123;

function mapcat(xs, f) {
  return xs.reduce((xs, x) => xs.concat(f(x)), []);
}

function getCVPaths(root) {
  return mapcat(fs.readdirSync(root), f => {
    if (/\.html$/.test(f)) {
      return [[path.basename(root), `http://localhost:${port}/cv/${path.basename(root)}/`]];
    } else if (fs.statSync(path.join(root, f)).isDirectory()) {
      return getCVPaths(path.join(root, f));
    }
  }).filter(v => v);
}

async function crawl(page, root) {
  for (const [name, url] of getCVPaths(root)) {
    const file = `${root}/${name}.pdf`;
    console.log(url, '=>', file);
    await html2pdf(page, url, file);
  }
}

const serve = serveStatic(process.argv[2]);

const server = http.createServer((req, res) => {
  serve(req, res, finalhandler(req, res));
});

async function makePDFs() {
  server.listen(port);
  const browser = await puppeteer.launch({
    args: ['--disable-dev-shm-usage',
           '--no-sandbox']
  });
  const page = await browser.newPage();
  await crawl(page, path.join(process.argv[2], 'cv'));
  await browser.close();
  server.close();
  process.exit(0);
}

makePDFs();
