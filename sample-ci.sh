#!/bin/bash -x

daemon_pid=
issuer_pid=
user_agent_pid=
verifier_pid=
top_path=$(pwd)

getDaemonPid(){
   daemon_pid=`ps aux|grep "DemoCommand" | grep "daemon" | grep -v grep|awk '{print $2}'|head -1` 
}
getIssuerPid(){
   issuer_pid=`ps aux|grep "DemoCommand" | grep "issuer" | grep -v grep|awk '{print $2}'|head -1` 
}
getUserAgentPid(){
   user_agent_pid=`ps aux|grep "DemoCommand" | grep "user_agent" | grep -v grep|awk '{print $2}'|head -1` 
}
getVerifierPid(){
   verifier_pid=`ps aux|grep "DemoCommand" | grep "verifier" | grep -v grep|awk '{print $2}'|head -1` 
}


function daemon(){
    getDaemonPid;
    if [ -n "$daemon_pid" ];then
        kill -9 $daemon_pid
    fi
    sed -i "/^blockchain.orgid/cblockchain.orgid=organizationA" $top_path/dist/conf/weidentity.properties
    ./command.sh daemon
    sleep 2
    getDaemonPid;
    if [ -z "$daemon_pid" ];then
        echo "start daemon process failed."
        exit 1
    else
        echo "start daemon process success."
    fi
}

function issuer(){
    echo "begin to start issuer"
    getIssuerPid;
    if [ -n "$issuer_pid" ];then
        kill -9 $issuer_pid
    fi

    sed -i "/^blockchain.orgid/cblockchain.orgid=organizationB" $top_path/dist/conf/weidentity.properties
    sed -i "/^nodes/cnodes=$NODE2_IP" $top_path/dist/conf/weidentity.properties
    
    ./command.sh issuer
    if [ $? -eq 0 ] && [ -e $top_path/tmp/credentials.json ];then
        echo "execute issuer success."       
    else
        echo "execute issuer failed."
        kill_daemon
        exit 1
    fi
    echo "execute issuer finished"
}

function user_agent(){
    echo "begin to start user_agent"
    getUserAgentPid;
    if [ -n "$user_agent_pid" ];then
        kill -9 $user_agent_pid
    fi
    ./command.sh user_agent
    if [ $? -eq 0 ] && [ -e $top_path/tmp/temp.data ];then
        echo "execute user_agent success."       
    else
        echo "execute user_agent failed."
        kill_daemon
        exit 1
    fi
    echo "execute user_agent finished"
}

function verifier(){
    echo "begin to start verifier"
    getVerifierPid;
    if [ -n "$verifier_pid" ];then
        kill -9 $verifier_pid
    fi
    ./command.sh verifier
    if [ $? -ne 0 ];then
        echo "execute verifier failed."
        kill_daemon
        exit 1
    fi
    echo "execute verifier finished"
}

function kill_daemon(){
    getDaemonPid;
    if [ -n "$daemon_pid" ];then
        kill -9 $daemon_pid
    fi
}

function main(){
    
    if [ -d $top_path/tmp ];then
        rm -rf $top_path/tmp
    fi
    #start daemon process 
    daemon;
    #start issuer
    issuer;
    #start user_agent
    user_agent;
    #start verifier
    verifier;  
    kill_daemon;
}

main

