#!/bin/bash
{
    # should run this script when you want it to be able to update (like during late hours, or perhaps just once every hour if downtime during the day doesn't matter much)

    set -uo pipefail
    PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

    DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
    cd "$DIR"

    BRANCH="$(git rev-parse --abbrev-ref HEAD)"

    exec {LOCK_UPDATE}> ./_LOCKER__UPDATE_ || exit 0
    flock -n "$LOCK_UPDATE" || { echo "The update is already pulling..."; exit 0; }
    {
        git fetch origin "$BRANCH"
        if [ "$(git rev-parse HEAD)" = "$(git rev-parse origin/$BRANCH)" ]; then
            echo "Up-to-date!"
        else
            echo "Update is available, shutting down the server..."
            sleep 5
            rm ./_ACTION__SHUTDOWN_
        fi
    }
    flock -u "$LOCK_UPDATE"
} && exit 0
