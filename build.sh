#!/bin/bash

function format-date() {
    timestamp=$1
    format="$2"
    res=$(date -d @$timestamp +"$format" 2> /dev/null)

    if [ $? -eq 0 ]; then
        echo $res
    else
        date -r $timestamp -u +"$format"
    fi
}

bucket="s3://kodemaker-www/"
target="build"

echo "Syncing down production site for accurate build diffs"
mkdir -p "$target"
pushd "$target" > /dev/null
aws s3 sync $bucket .
popd > /dev/null

echo "Building site"
diffs=$(lein build-site :json)

echo "Syncing assets, cacheable for a year"
pushd "$target" > /dev/null

ts=$(date -d "+1 year" +%s 2> /dev/null)

if [ $? -ne 0 ]; then
  ts=$(date -v +1y +%s)
fi

expires="$(format-date $ts "%a, %d %b %Y %H:%M:%S GMT")"
aws s3 sync . $bucket --expires "$expires" --exclude "*" --include "assets/*"

echo "Syncing remaining files and deleting removed files"
aws s3 sync . $bucket --delete --exclude "assets/*"

echo "Purging old assets"
aws s3 sync . $bucket --expires "$expires" --exclude "*" --include "assets/*" --delete

changed=$(for file in $(echo "${diffs}" | jq -cr '.changed[]'); do
            echo ${file/"index.html"/""}
          done)

removed=$(for file in $(echo "${diffs}" | jq -cr '.removed[]'); do
            echo ${file/"index.html"/""}
          done)

if [ "$changed$removed" != "" ]; then
  paths=$(echo "$changed $removed")
  echo "Purging Cloudfront caches for $paths"
  aws cloudfront create-invalidation --distribution-id E377BQUYES9DH7 --paths $paths
fi
