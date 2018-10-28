const fs = require('fs');
const path = require('path');
const {execSync} = require('child_process');
const prince = process.env.PRINCE_PATH ? `${process.env.PRINCE_PATH}/prince` : 'prince';

function mkdir(dir) {
  try {
    fs.mkdirSync(dir);
  } catch (e) {}
}

const outDir = 'cv-pdf';
const public = 'resources/public';
mkdir(outDir);

function mkpdf(root, filepath) {
  const [file, ...dirs] = filepath.split('/').reverse();
  let p = outDir;

  dirs.reverse().forEach(d => {
    p = path.join(p, d);
    mkdir(p);
  });

  const cvhtml = path.join(p, path.basename(filepath));
  console.log('Write', cvhtml);

  fs.writeFileSync(cvhtml,
                   fs.readFileSync(path.join(root, filepath), 'utf-8')
                   .replace(/="\//g, `="${path.relative(p, 'build')}/`),
                   'utf-8');

  const output = cvhtml.replace(outDir, root).replace(/\/index\.html$/, '.pdf');
  console.log('Output PDF to', output);
  execSync(`${prince} ${cvhtml} -o ${output}`);
}

function crawl(root) {
  fs.readdirSync(root).forEach(f => {
    if (/\.html$/.test(f)) {
      mkpdf(root, f);
    } else if (fs.statSync(path.join(root, f)).isDirectory()) {
      crawl(path.join(root, f));
    }
  });
}

crawl('build/cv');
crawl('build/ny/cv');
