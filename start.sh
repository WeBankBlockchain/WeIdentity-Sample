#!/bin/bash
nohup java -cp dist/app/*:dist/conf/:dist/lib/* com.webank.weid.demo.server.SampleApp &
tail -f nohup.out
