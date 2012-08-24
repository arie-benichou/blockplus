#!/bin/sh
netstat -apn|grep :8080
kill -9 $( lsof -i:8080 -t )
mvn clean
mvn package
mvn jetty:run-war & echo "\007" && sleep 3 && echo "\007" && sleep 2 && echo "\007" && sleep 1 && echo "\007" && google-chrome localhost:8080/blockplus/home
