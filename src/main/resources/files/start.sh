#!/bin/bash -e

JAVA_BIN=`which java`
JAVA_OPTS=$(sed -e '/^[[:space:]]*\/\//d' -e 's|[[:space:]]*//.*| |' -e 's|^| |' {{{path.jvmConfigFile}}} | tr -d "\n")
JAVA_CMD="${JAVA_BIN} ${JAVA_OPTS} -jar {{{path.jarFile}}} server {{{path.configFile}}}"

logger -is "[`date -u +%Y-%m-%dT%T.%3NZ`] Starting {{{project.name}}}"
exec ${JAVA_CMD} 2>&1 | tee -a {{{path.logDirectory}}}/init.log
