#!/bin/sh

netstat -apn|grep :8080

if [ "$MVN_HOME" != "" ] ; then
	mvn clean
	mvn package
fi

kill -9 $( lsof -i:8080 -t )

java -jar ./target/blockplus-jar-with-dependencies.jar & sleep 3 && echo "\007" && google-chrome localhost:8080/blockplus/