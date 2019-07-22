#!/bin/bash -x
daemon_pid=
if [[ "$1" != "issuer" ]];then
  if [[ "$1" != "user_agent" ]];then
     if [[ "$1" != "verifier" ]];then
        if [[ "$1" != "daemon" ]];then
           echo "please input the correct parameters:issuer, user_agent, verifier, daemon"
           exit 1
        fi
    fi
  fi
fi

getDaemonPid(){
   daemon_pid=`ps aux|grep "DemoCommand" | grep "daemon" | grep -v grep|awk '{print $2}'|head -1` 
}

if [[ "$1" == daemon ]];then
	getDaemonPid
	if [ -n "$daemon_pid" ];then
       echo "the AMOP server already start."
       exit 1
    fi
    
    java -cp dist/conf/:dist/lib/*:dist/app/* com.webank.weid.demo.command.DemoCommand $1 >/dev/null 2>&1 &
    
    sleep 2
    
    getDaemonPid
    if [ -n "$daemon_pid" ];then
       echo "the AMOP server start success."
    else
       echo "the AMOP server start fail."
    fi
   
else
    echo "--------- start $1 ----------"
    java -cp dist/conf/:dist/lib/*:dist/app/* com.webank.weid.demo.command.DemoCommand $1
fi

