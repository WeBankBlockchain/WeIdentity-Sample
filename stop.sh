#!/bin/bash
sample_pid=

getPid() {
   sample_pid=`ps aux|grep "SampleApp" | grep -v grep|awk '{print $2}'|head -1` 
}

getPid;

if [ -n "$sample_pid" ];then
   kill -9 $sample_pid
   echo "the server stop success."
else 
   echo "the server already stop."
fi