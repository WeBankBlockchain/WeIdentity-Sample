#!/bin/bash
nohup java -cp dist/app/*:dist/conf/:dist/lib/* com.webank.weid.demo.server.SampleApp >./dist/logs/all.log 2>&1 &
tail -f ./dist/logs/all.log
