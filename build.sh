#!/bin/bash

echo "begin to build weidentity-sample."

gradle clean build -x checkMain
if [[ $? -ne 0 ]];then
	echo "gradle build weidentity-sample failed"
	exit 1
fi
echo "build weidentity-sample success."
cp -fr libs/* dist/lib/

touch dist/logs/all.log

exit 0
