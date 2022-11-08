package com.webank.weid.demo.service.impl;

import com.webank.weid.util.DataToolUtils;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
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

        CryptoKeyPair keyPair = DataToolUtils.cryptoSuite.createKeyPair(privateKey);
        String publicKey = DataToolUtils.hexStr2DecStr(keyPair.getHexPublicKey());

        return new ResponseData<>(publicKey, ErrorCode.SUCCESS);
    }
}
