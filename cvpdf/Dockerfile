FROM node:alpine

RUN apk add --no-cache \
      chromium \
      nss \
      freetype \
      freetype-dev \
      harfbuzz \
      ca-certificates \
      ttf-freefont

ADD cv2pdf.js /cvpdf/cv2pdf.js
ADD package.json /cvpdf/package.json

# Tell Puppeteer to skip installing Chrome. We'll be using the installed package.
ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true \
    PUPPETEER_EXECUTABLE_PATH=/usr/bin/chromium-browser

RUN cd /cvpdf \
    && npm install

ENTRYPOINT ["/usr/local/bin/node", "/cvpdf/cv2pdf.js"]
