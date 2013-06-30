#!/bin/sh
cat src/main/resources/www/private/modules/*.js > src/main/resources/www/public/library/blockplus.js
cat src/main/resources/www/private/modules/**/*.js >> src/main/resources/www/public/library/blockplus.js
java -jar externals/closure-compiler.jar --js src/main/resources/www/public/library/blockplus.js > src/main/resources/www/public/library/blockplus.min.js 