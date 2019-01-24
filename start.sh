#!/bin/bash
nohup java -cp dist/app/*:dist/conf/:dist/lib/* com.webank.demo.server.SampleApp &
tail -f dist/logs/all.log