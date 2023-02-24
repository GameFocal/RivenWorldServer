#!/bin/bash
{
    # should run this script once every minute or so, as well as on boot

    set -uo pipefail
    PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

    DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
    cd "$DIR"

    BRANCH="$(git rev-parse --abbrev-ref HEAD)"

    exec {LOCK_RUN}> ./_LOCKER__RUN_ || exit 0
    flock -n "$LOCK_RUN" || { echo "Server is still running..."; exit 0; }
    {
        if [ -f "./_STATUS__IS_RUNNING_" ]; then
            PID="$(cat ./_STATUS__IS_RUNNING_)"
            if [ "$(ps -p $PID -o comm=)" = "java" ]; then
                echo "Server is still running..."
                exit 0
            fi
            rm ./_STATUS__IS_RUNNING_
            sleep 10
        fi

        exec {LOCK_UPDATE}> ./_LOCKER__UPDATE_ || exit 0
        flock -w 60 "$LOCK_UPDATE" || { echo "The update is already pulling..."; exit 0; }
        {
            git fetch origin "$BRANCH"
            if [ "$(git rev-parse HEAD)" = "$(git rev-parse origin/$BRANCH)" ]; then
                echo "Up-to-date!"

                sleep 5
                if [ -f "./_STATUS__IS_RUNNING_" ]; then
                    PID="$(cat ./_STATUS__IS_RUNNING_)"
                    if [ "$(ps -p $PID -o comm=)" = "java" ]; then
                        echo "Server is still running..."
                        exit 0
                    fi
                    rm ./_STATUS__IS_RUNNING_
                    sleep 10
                fi
            else
                sleep 5
                if [ -f "./_STATUS__IS_RUNNING_" ]; then
                    PID="$(cat ./_STATUS__IS_RUNNING_)"
                    if [ "$(ps -p $PID -o comm=)" = "java" ]; then
                        echo "Update is available, shutting down the server..."
                        rm ./_ACTION__SHUTDOWN_
                        exit 0
                    fi
                    rm ./_STATUS__IS_RUNNING_
                    sleep 10
                fi

                echo "Update is available, pulling it..."
                git reset --hard "origin/$BRANCH"
                chmod -R 0777 .

                sleep 5
                if [ -f "./_STATUS__IS_RUNNING_" ]; then
                    PID="$(cat ./_STATUS__IS_RUNNING_)"
                    if [ "$(ps -p $PID -o comm=)" = "java" ]; then
                        echo "Update is available, shutting down the server..."
                        rm ./_ACTION__SHUTDOWN_
                        exit 0
                    fi
                    rm ./_STATUS__IS_RUNNING_
                    sleep 10
                fi

                echo "Running the updated run.sh..."
                flock -u "$LOCK_UPDATE"
                flock -u "$LOCK_RUN"
                bash ./run.sh
                exit 0
            fi
        }
        flock -u "$LOCK_UPDATE"

        echo "Starting the server..."
        JAVA_ARGS=""
        JAVA_ENV="LOWENTRY_DOTENV_MD5='$(md5sum -b './.env' | cut -d' ' -f1)'"
        readarray LOWENTRY_DOTENV_ARRAY <<< "$(cat './.env' | sed -e "s/'/'\\\''/g")"
        LOWENTRY_MIN_MEMORY=""
        LOWENTRY_MAX_MEMORY=""
        for i in "${LOWENTRY_DOTENV_ARRAY[@]}"; do
            lowentry_dotenv_key="$(echo "$(cut -d '=' -f 1 <<< "$i")" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
            if [[ "$lowentry_dotenv_key" == '#'* ]]; then
                continue
            fi
            lowentry_dotenv_value=""
            if [[ "$i" == *'='* ]]; then
                lowentry_dotenv_value="$(echo "$(cut -d '=' -f 2- <<< "$i")" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
            fi
            if [ -n "$lowentry_dotenv_key" ]; then
                if [ "$lowentry_dotenv_key" = "LOWENTRY_MIN_MEMORY" ]; then
                    if [ -n "$lowentry_dotenv_value" ]; then
                        LOWENTRY_MIN_MEMORY="-Xms${lowentry_dotenv_value}"
                    fi
                elif [ "$lowentry_dotenv_key" = "LOWENTRY_MAX_MEMORY" ]; then
                    if [ -n "$lowentry_dotenv_value" ]; then
                        LOWENTRY_MAX_MEMORY="-Xmx${lowentry_dotenv_value}"
                    fi
                elif [[ "$lowentry_dotenv_key" == '-'* ]]; then
                    if [ -z "$lowentry_dotenv_value" ]; then
                        JAVA_ARGS+=" ${lowentry_dotenv_key}"
                    else
                        JAVA_ARGS+=" ${lowentry_dotenv_key}=${lowentry_dotenv_value}"
                    fi
                elif [ -n "$lowentry_dotenv_value" ]; then
                    JAVA_ENV+=" ${lowentry_dotenv_key}='${lowentry_dotenv_value}'"
                fi
            fi
        done
        nohup bash -c "$JAVA_ENV java -server $LOWENTRY_MIN_MEMORY $LOWENTRY_MAX_MEMORY $JAVA_ARGS -jar ../server.jar 2> ../_OUTPUT__ERRORS_ > ../_OUTPUT__LOG_" > /dev/null 2>&1 &
        sleep 10
    }
    flock -u "$LOCK_RUN"
} && exit 0
