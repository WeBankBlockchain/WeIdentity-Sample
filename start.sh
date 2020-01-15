#!/bin/bash
JAVA_OPTS='-Djdk.tls.namedGroups="secp256r1,secp256k1"'
nohup java ${JAVA_OPTS} -cp dist/app/*:dist/conf/:dist/lib/* com.webank.weid.demo.server.SampleApp >./dist/logs/all.log 2>&1 &
tail -f ./dist/logs/all.log
