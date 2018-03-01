const fs = require('fs');
const path = require('path');
const {execSync} = require('child_process');
const prince = process.env.PRINCE_PATH ? `${process.env.PRINCE_PATH}/prince` : 'prince;'

function mkdir(dir) {
  try {
    fs.mkdirSync(dir);
  } catch (e) {}
}

const rootDir = 'build/cv';
const outDir = 'cv-pdf';
const public = 'resources/public';
mkdir(outDir);

function mkpdf(filepath) {
  const [file, ...dirs] = filepath.replace(`${rootDir}/`, '').split('/').reverse();
  let p = outDir;

  dirs.reverse().forEach(d => {
    p = path.join(p, d);
    mkdir(p);
  });

  const cvhtml = path.join(p, path.basename(filepath));
  console.log('Write', cvhtml);

  fs.writeFileSync(cvhtml,
                   fs.readFileSync(filepath, 'utf-8')
                   .replace(/="\//g, `="${path.relative(p, 'build')}/`)
                   .replace(/"[^"]+cv-print.css"/, `"${path.relative(p, public)}/styles/cv-print-prince.css"`),
                   'utf-8');

  const output = cvhtml.replace(outDir, rootDir).replace(/\/index\.html$/, '.pdf');
  console.log('Output PDF to', output);
  execSync(`${prince} ${cvhtml} -o ${output}`);
}

function crawl(root) {
  fs.readdirSync(root).forEach(f => {
    if (/\.html$/.test(f)) {
      mkpdf(path.join(root, f));
    } else if (fs.statSync(path.join(root, f)).isDirectory()) {
      crawl(path.join(root, f));
    }
  });
}

crawl(rootDir);
