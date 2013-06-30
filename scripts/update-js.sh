#!/bin/sh
cat ./src/main/resources/www/private/modules/*.js > ./src/main/resources/www/blockplus.js
cat ./src/main/resources/www/private/modules/**/*.js >> ./src/main/resources/www/blockplus.js
java -jar externals/closure-compiler.jar --js src/main/resources/www/blockplus.js > src/main/resources/www/public/library/blockplus.min.js 