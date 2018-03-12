#!/bin/bash

export JETTY_LOGS=$SKELETON_HOME/log

export JETTY_STATE=$JETTY_HOME/jetty.state

export JAVA_OPTIONS="$JAVA_OPTIONS -Djetty.home=$JETTY_HOME -Djetty.base=$JETTY_BASE -Djetty.state=$JETTY_STATE -Djetty.logs=$JETTY_LOGS -Dpath=$ADD_TO_CLASSPATH"

