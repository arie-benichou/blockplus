#!/bin/sh

WHICH_MAVEN=$(which mvn);

if $WHICH_MAVEN >/dev/null ; then
	echo "Maven not found.";
else
	./scripts/update-javacript-modules.sh;	
	mvn jetty:run;
fi