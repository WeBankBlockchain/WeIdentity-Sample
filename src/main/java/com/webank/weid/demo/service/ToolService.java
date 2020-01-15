package com.webank.weid.demo.service;

import com.webank.weid.protocol.response.ResponseData;

/**
 * 工具服务.
 * @author darwindu
 * @date 2020/1/8
 **/
public interface ToolService {

    ResponseData<String> getPublicKey(String privateKey);
}
