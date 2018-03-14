#!/bin/bash

export SKELETON_LOG_DIR=$SKELETON_HOME/log

#Use resources (configuration files) from the conf directory:
export ADD_TO_CLASSPATH=$SKELETON_HOME/conf

if [ ! -z "$CLASSPATH" ] ; then
	export CLASSPATH=$CLASSPATH:$ADD_TO_CLASSPATH
else
	export CLASSPATH=$ADD_TO_CLASSPATH
fi

JAVA_HEAP_OPTS="-Xms1024m -Xmx2048m -XX:MaxMetaspaceSize=512m -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=HeapDump.txt"
#DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"

#JMX_OPTS="-Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djmx.http.port=39900 -Dcom.sun.management.jmxremote.port=1099 -Djmx.invoke.getters=true -Djava.rmi.server.hostname=< jetty server ip>"


export JAVA_OPTS="$JAVA_OPTS -Dskeleton.home=$SKELETON_HOME $JAVA_HEAP_OPTS $DEBUG_OPTS"
export JAVA_OPTIONS="$JAVA_OPTIONS -Dfile.encoding=UTF-8 -Dskeleton.home=$SKELETON_HOME $JAVA_HEAP_OPTS $DEBUG_OPTS"
#export JAVA_OPTIONS="$JAVA_OPTIONS -Dfile.encoding=UTF-8 -Dskeleton.home=$SKELETON_HOME $JAVA_HEAP_OPTS $DEBUG_OPTS $JMX_OPTS"
