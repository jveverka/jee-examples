#!/bin/bash

### BEGIN INIT INFO
# Provides:          device-controller
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: hybrid-app-rpi-daemon initscript
# Description:       This service is used to run web-socket client on RPi
### END INIT INFO

# install:
# cd /etc/init.d
# ln -s /opt/hybrid-app-rpi-1.0.0001/bin/hybrid-app-rpi-daemon.sh hybrid-app-rpi-daemon
# update-rc.d hybrid-app-rpi-daemon defaults

export APP_HOME=/opt/hybrid-app-rpi-1.0.0001

export CLASSPATH=$APP_HOME/lib/hybrid-app-rpi-1.0.0001.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/jersey-client-2.22.2.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/gson-2.7.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/guava-19.0.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/protobuf-java-3.0.2.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/protobuf-java-util-3.0.2.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/tyrus-standalone-client-1.13.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/guice-4.1.0.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/pi4j-core-1.1.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/hybrid-app-common-1.0.0001.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/hybrid-app-common-client-1.0.0001.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/itx-rpi-drivers-1.0.0.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/javax.ws.rs-api-2.0.1.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/jersey-common-2.22.2.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/hk2-api-2.4.0-b34.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/javax.inject-2.4.0-b34.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/hk2-locator-2.4.0-b34.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/javax.inject-1.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/aopalliance-1.0.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/testng-6.9.13.6.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/javax.annotation-api-1.2.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/jersey-guava-2.22.2.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/osgi-resource-locator-1.0.1.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/hk2-utils-2.4.0-b34.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/aopalliance-repackaged-2.4.0-b34.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/javassist-3.18.1-GA.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/jcommander-1.48.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/ant-1.9.7.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/bsh-2.0b4.jar
export CLASSPATH=$CLASSPATH:$APP_HOME/lib/ant-launcher-1.9.7.jar

LOGGER_PROPS=-Djava.util.logging.config.file=$APP_HOME/bin/logging.properties
DC_PID=`ps -ef | grep java | grep hybrid-app | awk '{ print $2 }'`
JVM_OPTS="-DserverAddress=192.168.30.158:8080 -DdeviceId=RPiDevice"
case "$1" in 
    start)
       if [ "xx$DC_PID" == "xx" ]; then
          echo "Starting device-controller"
          java -classpath $CLASSPATH $LOGGER_PROPS $JVM_OPTS itx.hybridapp.rpi.Main >/dev/null 2>&1 &
       else
          echo "device-controller is already running pid=$DC_PID" 
       fi
    ;;
    stop)
       if [ "xx$DC_PID" == "xx" ]; then
          echo "devive-controller is already stopped"
       else 
          echo "Stopping device-controller"
          kill -9 $DC_PID
       fi 
    ;;
    status)
       if [ "xx$DC_PID" == "xx" ]; then
          echo "device-controller is stopped"
       else 
          echo "device-controller is running pid=$DC_PID"
       fi
    ;;
    *)
       echo "Usage: $0 start|status|stop"
       exit 1
    ;;

esac

exit 0

