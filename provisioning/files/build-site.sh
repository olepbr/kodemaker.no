#!/bin/sh

cd

timestamp() {
    date -u +"%Y-%m-%dT%H:%M:%SZ"
}

log () {
    echo $1
    echo "$(timestamp): $1" >> build.log
}

if [ ! -d "kodemaker.no" ]; then
    git clone https://github.com/kodemaker/kodemaker.no.git
    changed=1
fi

cd kodemaker.no

if [ -f "in-progress.tmp" ]; then
    log "Build in progress, aborting"
else
    touch in-progress.tmp

    current_head=$(git rev-parse HEAD)
    git fetch
    git reset --hard origin/master > /dev/null

    if [ "$current_head" != $(git rev-parse HEAD) ] || [ ! -f /var/www/kodemaker.no/current/index.html ]; then
        ../bin/lein with-profile test midje
        if [ $? -eq 0 ]; then
            log "Building"
            ../bin/lein build-site
            if [ $? -eq 0 ]; then
                log "Publishing"
                rm -rf /var/www/kodemaker.no/current
                mv build /var/www/kodemaker.no/current
                log "Purging cache"
                varnishadm -S /etc/varnish/secret -T localhost:6082 "ban req.url ~ (/|[.]html)$"
                log "Done!"
            else
                log "Build failed, aborting."
            fi
        else
            log "Test failures, aborting build."
        fi
    else
        log "Site is up to date."
    fi

    rm in-progress.tmp
fi
