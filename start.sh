#!/bin/bash
nohup java -cp 'conf/:lib/*:app/*' com.webank.weid.demo.server.SampleApp>./logs/all.log 2>&1 &
tail -f ./logs/all.log
