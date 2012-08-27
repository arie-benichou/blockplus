#!/bin/sh
if [ "$MVN_HOME" = "" ] ; then
{
	echo "System variable 'MVN_HOME' is not set.";
}
else	
{
	mvn clean package;
}
fi
echo "DONE"