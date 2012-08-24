#!/bin/sh
mvn package
mvn jetty:run-war
echo "\007"
google-chrome localhost:8080/blockplus/home
