#!/bin/sh

#echo ${MVN_HOME}
#echo ${PATH}

#netstat -apn|grep :8080

kill -9 $( lsof -i:8080 -t )

mvn clean
mvn package

mvn jetty:run-war & sleep 5 && echo "\007" && google-chrome localhost:8080/blockplus/home