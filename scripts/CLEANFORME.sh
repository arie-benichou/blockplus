#!/bin/sh
netstat -apn|grep :8080;
kill -9 $( lsof -i:8080 -t );