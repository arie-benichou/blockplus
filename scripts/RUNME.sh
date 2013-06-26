#!/bin/sh

WHICH_MAVEN=$(which mvn);

if $WHICH_MAVEN >/dev/null ; then
	echo "Maven not found.";
else
	cat ./src/main/resources/www/data/modules/*.js > ./src/main/resources/www/data/modules.js
	cat ./src/main/resources/www/data/modules/**/*.js >> ./src/main/resources/www/data/modules.js
	mvn jetty:run;
fi