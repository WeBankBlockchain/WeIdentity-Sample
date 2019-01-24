#!/bin/bash
if [[ $1 != 'issuer' ]];then
  if [[ $1 != 'user' ]];then
     if [[ $1 != 'verifier' ]];then
     	echo "请输入正确的参数，分别为issuer，user，verifier"
   	exit 
    fi
  fi
fi
java  -cp dist/conf/:dist/lib/*:dist/app/* com.webank.demo.command.DemoCommand $1
