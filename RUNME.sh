#!/bin/sh
	
if [ "$MVN_HOME" != "" ] ; then
	mvn clean
	mvn package
fi

netstat -apn|grep :8080
kill -9 $( lsof -i:8080 -t )

(java -jar ./target/blockplus-jar-with-dependencies.jar) \
	& (sleep 5) \
	&& (echo "\007") \
	&& (google-chrome localhost:8080/blockplus/)
	
#	&& ((google-chrome http://localhost:8080/blockplus/game.html) & (google-chrome localhost:8080/blockplus/))
