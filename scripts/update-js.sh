#!/bin/sh
cat ./src/main/resources/www/data/modules/*.js > ./src/main/resources/www/data/debug.js
cat ./src/main/resources/www/data/modules/**/*.js >> ./src/main/resources/www/data/debug.js
java -jar externals/closure-compiler.jar --js src/main/resources/www/data/debug.js > src/main/resources/www/data/modules.js 