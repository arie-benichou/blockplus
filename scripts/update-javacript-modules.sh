#!/bin/sh
cat ./src/main/resources/www/data/modules/*.js > ./src/main/resources/www/data/modules.js
cat ./src/main/resources/www/data/modules/**/*.js >> ./src/main/resources/www/data/modules.js
