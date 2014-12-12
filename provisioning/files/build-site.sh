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

    git pull | grep -q -v 'Already up-to-date.' && changed=1

    if [ $changed ]; then
        ../bin/lein with-profile test midje :filter -slow && ../bin/lein with-profile test midje :filter slow && passed=1
        if [ $passed ]; then
            log "Building"
            ../bin/lein build-site && built=1
            if [ $built ]; then
                log "Publishing"
                rm -rf /var/www/kodemaker.no/current
                mv build /var/www/kodemaker.no/current
                log "Purging cache"
                varnishadm -S /etc/varnish/secret -T localhost:6082 "purge req.url ~ (/|[.]html)$"
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
