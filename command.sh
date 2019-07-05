#!/bin/bash

if [[ $1 != 'issuer' ]];then
  if [[ $1 != 'user_agent' ]];then
     if [[ $1 != 'verifier' ]];then
        if [[ $1 != 'daemon' ]];then
           echo "please input the correct parameters:issuer, user_agent, verifier, daemon"
           exit 1
        fi
    fi
  fi
fi

if [[ $1 = 'daemon' ]];then
    java -cp dist/conf/:dist/lib/*:dist/app/* com.webank.weid.demo.command.DemoCommand $1 >/dev/null 2>&1 &
else
    java -cp dist/conf/:dist/lib/*:dist/app/* com.webank.weid.demo.command.DemoCommand $1
fi
