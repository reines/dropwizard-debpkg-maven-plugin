#!/bin/bash

### BEGIN INIT INFO
# Provides:             {{{project.artifactId}}}
# Required-Start:
# Required-Stop:
# Default-Start:        2 3 4 5
# Default-Stop:         0 1 6
# Short-Description:    {{{project.name}}} - {{{project.description}}}
### END INIT INFO

. /lib/lsb/init-functions

PID_FILE="/var/run/{{{project.artifactId}}}.pid"

do_start() {
    start-stop-daemon --start --quiet --pidfile "${PID_FILE}" --exec "{{{path.startScript}}}" --test > /dev/null || return 1
    start-stop-daemon --start --quiet --pidfile "${PID_FILE}" --chuid "{{{unix.user}}}:{{{unix.user}}}" --exec "{{{path.startScript}}}" --make-pidfile --background --name "{{{project.artifactId}}}" || return 2
    return 0
}

do_stop() {
    start-stop-daemon --stop --quiet --retry=TERM/30/KILL/5 --pidfile "${PID_FILE}" --name "{{{project.artifactId}}}"
    RETVAL="$?"
    [ "${RETVAL}" = 2 ] && return 2
    rm -f "${PID_FILE}"
    return "${RETVAL}"
}

case "$1" in
    start)
        do_start
        exit $?
        ;;
    stop)
        do_stop
        exit $?
        ;;
    status)
        status_of_proc -p "${PID_FILE}" "{{{path.startScript}}}" "{{{project.artifactId}}}"
        exit $?
        ;;
    restart|reload)
        do_stop
        do_start
        exit $?
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|reload|status}"
        exit 1
esac

exit 0
