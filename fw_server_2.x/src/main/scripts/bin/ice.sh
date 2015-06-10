#!/bin/sh
bin=`dirname $0`
cd $bin && cd ..
source /etc/profile
servername='ifw-ice-server'
mainclass='com.github.ipaas.ifw.server.ice.ComponentServer'
varpid=`ps -ef|grep $mainclass |grep -v grep|awk '{print $2}'`
libpath=`ls ./lib/*.jar | tr '\n' ':'`
distpath=`ls ./dist/*.jar | tr '\n' ':'`
classpath='./resources/:'${libpath}:${distpath}
stdlog='/www/applog/ifw_server/console.log'
jvmarg='-Xmx2048m -Xms2048m -Xmn1024m -Xss128k -XX:PermSize=128m -XX:MaxPermSize=128m -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false 
-Dcom.sun.management.jmxremote.port=11020 -Djava.rmi.server.hostname=127.0.0.1'
fullcmd="java $jvmarg -classpath ${classpath} ${mainclass} > $stdlog 2>&1 &"

case "$1" in
restart)
	#restart action
	if test "x$varpid" != "x"
	then
	  kill -9 $varpid
	  sleep 1
	fi
	eval $fullcmd
	echo "$servername restarted" 
	;;
stop)
	#stop action
	if test "x$varpid" = "x"
	then
	  echo "fail. No $servername can be stopped."
	  exit
	fi
	kill -9 $varpid 
	echo "$servername stopped"
	;; 
start)
	#start action, default option
	if test "x$varpid" != "x"
	then
	  echo "fail. $servername is running already."
	  exit
	fi
	eval $fullcmd
	echo "$servername started"
	;;
*)
	echo "Usage: $0 {start|stop|restart}"
	exit 1
	;;       
esac      