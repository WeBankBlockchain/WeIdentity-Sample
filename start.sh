#!/bin/bash
JAVA_OPTS='-Djdk.tls.namedGroups="secp256r1,secp256k1"'
nohup java ${JAVA_OPTS} -cp dist/conf/:dist/lib/*:dist/app/* com.webank.weid.demo.server.SampleApp >./logs/all.log 2>&1 &
tail -f ./logs/all.log
