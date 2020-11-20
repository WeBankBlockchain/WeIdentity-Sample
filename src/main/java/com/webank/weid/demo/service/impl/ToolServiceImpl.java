package com.webank.weid.demo.service.impl;

import java.math.BigInteger;

import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.springframework.stereotype.Service;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.service.ToolService;
import com.webank.weid.protocol.response.ResponseData;

/**
 * 工具类实现.
 * @author darwindu
 * @date 2020/1/8
 **/
@Service
public class ToolServiceImpl implements ToolService {

    @Override
    public ResponseData<String> getPublicKey(String privateKey) {

        ECKeyPair keyPair = ECKeyPair.create(new BigInteger(privateKey));
        return new ResponseData<>(keyPair.getPublicKey().toString(), ErrorCode.SUCCESS);
    }
}
