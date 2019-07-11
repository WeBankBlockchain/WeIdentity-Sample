#!/bin/bash
ps aux | grep com.webank.weid.demo.server.SampleApp |  awk '{print $2}' | xargs kill -9
