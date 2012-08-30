#!/bin/sh
#java -jar ./target/blockplus-jar-with-dependencies.jar;

WHICH_MAVEN=$(which mvn);

if $WHICH_MAVEN >/dev/null ; then
	echo "Maven not found.";
else
	mvn jetty:run;
fi