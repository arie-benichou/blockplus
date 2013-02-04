#!/bin/sh
netstat -apn|grep :8282;
kill -9 $( lsof -i:8282 -t );