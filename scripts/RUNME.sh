#!/bin/sh

WHICH_MAVEN=$(which mvn);

if $WHICH_MAVEN >/dev/null ; then
	echo "Maven not found.";
else
	./scripts/update-js.sh;
	mvn jetty:run;
fi