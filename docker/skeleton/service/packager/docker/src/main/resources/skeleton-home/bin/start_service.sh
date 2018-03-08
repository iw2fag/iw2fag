#!/bin/bash

#This script:
# 1) set's configuration parameters
# 2) fires up jetty (where our app is hopefully installed)
# 3) TERMINATES!

#print current user
echo "current user is:"
whoami  

#${SKELETON_HOME}/utils/init_service.sh

cd $JETTY_BASE
BIN_DIR=`dirname $0`

source $BIN_DIR/setenv_common.sh
source $BIN_DIR/setenv_jetty.sh


echo JETTY_HOME= $JETTY_HOME
echo JETTY_BASE= $JETTY_BASE
echo SKELETON_HOME= ${SKELETON_HOME}

exec java $JAVA_OPTIONS -jar $JETTY_HOME/start.jar --lib=${SKELETON_HOME}/conf 2>&1
