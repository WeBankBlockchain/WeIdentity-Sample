#!/bin/bash
if [[ $1 != 'issuer' ]];then
  if [[ $1 != 'user' ]];then
     if [[ $1 != 'verifier' ]];then
     	echo "please input the correct parameters:issuer, user, verifier"
   	exit 
    fi
  fi
fi
java  -cp dist/conf/:dist/lib/*:dist/app/* com.webank.weid.demo.command.DemoCommand $1