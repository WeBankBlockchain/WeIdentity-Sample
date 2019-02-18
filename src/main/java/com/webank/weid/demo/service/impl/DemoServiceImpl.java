/*
 *       Copyright© (2019) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-sample.
 *
 *       weidentity-sample is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-sample is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-sample.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.webank.weid.demo.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.AuthorityIssuer;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.base.WeIdPublicKey;
import com.webank.weid.protocol.request.CptMapArgs;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.request.CreateWeIdArgs;
import com.webank.weid.protocol.request.RegisterAuthorityIssuerArgs;
import com.webank.weid.protocol.request.SetAuthenticationArgs;
import com.webank.weid.protocol.request.SetPublicKeyArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.AuthorityIssuerService;
import com.webank.weid.rpc.CptService;
import com.webank.weid.rpc.CredentialService;
import com.webank.weid.rpc.WeIdService;

/**
 * Demo service.
 *
 * @author v_wbgyang
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private AuthorityIssuerService authorityIssuerService;

    @Autowired
    private CptService cptService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private WeIdService weIdService;
    
    /**
     * set validity period to 360 days by default.
     */
    private static final long EXPIRATION_DATE  = 1000L * 60 * 60 * 24 * 360;

    /**
     * create weId with public and private keys and set related properties.
     * 
     * @param publicKey public key
     * @param privateKey private key
     * @return returns the create weId
     */
    public ResponseData<String> createWeIdAndSetAttr(String publicKey, String privateKey) {

        // 1, create weId using the incoming public and private keys
        CreateWeIdArgs createWeIdArgs = new CreateWeIdArgs();
        createWeIdArgs.setPublicKey(publicKey);
        createWeIdArgs.setWeIdPrivateKey(new WeIdPrivateKey());
        createWeIdArgs.getWeIdPrivateKey().setPrivateKey(privateKey);
        ResponseData<String> createResult = weIdService.createWeId(createWeIdArgs);
        if (createResult.getErrorCode().intValue() != ErrorCode.SUCCESS.getCode()) {
            return createResult;
        }

        CreateWeIdDataResult weIdData = new CreateWeIdDataResult();
        weIdData.setWeId(createResult.getResult());
        weIdData.setUserWeIdPrivateKey(new WeIdPrivateKey());
        weIdData.getUserWeIdPrivateKey().setPrivateKey(privateKey);
        weIdData.setUserWeIdPublicKey(new WeIdPublicKey());
        weIdData.getUserWeIdPublicKey().setPublicKey(publicKey);

        // 2, call set public key
        ResponseData<Boolean> setPublicKeyRes = this.setPublicKey(weIdData);
        if (!setPublicKeyRes.getResult()) {
            createResult.setErrorCode(setPublicKeyRes.getErrorCode());
            createResult.setErrorMessage(setPublicKeyRes.getErrorMessage());
            return createResult;
        }

        // 3, call set authentication
        ResponseData<Boolean> setAuthenticateRes = this.setAuthenticate(weIdData);
        if (!setAuthenticateRes.getResult()) {
            createResult.setErrorCode(setAuthenticateRes.getErrorCode());
            createResult.setErrorMessage(setAuthenticateRes.getErrorMessage());
            return createResult;
        }
        return createResult;
    }

    /**
     * create weId and set related properties.
     * 
     * @return returns the create weId  and public private keys
     */
    @Override
    public ResponseData<CreateWeIdDataResult> createWeIdWithSetAttr() {

        // 1, create weId, this method automatically creates public and private keys
        ResponseData<CreateWeIdDataResult> createResult = weIdService.createWeId();
        LOGGER.info(
            "weIdService is result,errorCode:{},errorMessage:{}",
            createResult.getErrorCode(), createResult.getErrorMessage()
        );

        if (createResult.getErrorCode().intValue() != ErrorCode.SUCCESS.getCode()) {
            return createResult;
        }

        // 2, call set public key
        ResponseData<Boolean> setPublicKeyRes = this.setPublicKey(createResult.getResult());
        if (!setPublicKeyRes.getResult()) {
            createResult.setErrorCode(setPublicKeyRes.getErrorCode());
            createResult.setErrorMessage(setPublicKeyRes.getErrorMessage());
            return createResult;
        }

        // 3, call set authentication
        ResponseData<Boolean> setAuthenticateRes = this.setAuthenticate(createResult.getResult());
        if (!setAuthenticateRes.getResult()) {
            createResult.setErrorCode(setAuthenticateRes.getErrorCode());
            createResult.setErrorMessage(setAuthenticateRes.getErrorMessage());
            return createResult;
        }
        return createResult;
    }

    /**
     * set public key.
     */
    private ResponseData<Boolean> setPublicKey(CreateWeIdDataResult createWeIdDataResult) {

        SetPublicKeyArgs setPublicKeyArgs = new SetPublicKeyArgs();
        setPublicKeyArgs.setWeId(createWeIdDataResult.getWeId());
        setPublicKeyArgs.setPublicKey(createWeIdDataResult.getUserWeIdPublicKey().getPublicKey());
        setPublicKeyArgs.setType("secp256k1");
        setPublicKeyArgs.setUserWeIdPrivateKey(new WeIdPrivateKey());
        setPublicKeyArgs.getUserWeIdPrivateKey()
            .setPrivateKey(createWeIdDataResult.getUserWeIdPrivateKey().getPrivateKey());

        ResponseData<Boolean> setResponse = weIdService.setPublicKey(setPublicKeyArgs);
        LOGGER.info(
            "setPublicKey is result,errorCode:{},errorMessage:{}",
            setResponse.getErrorCode(), 
            setResponse.getErrorMessage()
        );

        return setResponse;
    }

    /**
     * set authentication.
     */
    private ResponseData<Boolean> setAuthenticate(CreateWeIdDataResult createWeIdDataResult) {

        SetAuthenticationArgs setAuthenticationArgs = new SetAuthenticationArgs();
        setAuthenticationArgs.setWeId(createWeIdDataResult.getWeId());
        setAuthenticationArgs.setType("RsaSignatureAuthentication2018");
        setAuthenticationArgs
            .setPublicKey(createWeIdDataResult.getUserWeIdPublicKey().getPublicKey());
        setAuthenticationArgs.setUserWeIdPrivateKey(new WeIdPrivateKey());
        setAuthenticationArgs.getUserWeIdPrivateKey()
            .setPrivateKey(createWeIdDataResult.getUserWeIdPrivateKey().getPrivateKey());

        ResponseData<Boolean> setResponse = weIdService.setAuthentication(setAuthenticationArgs);
        LOGGER.info(
            "setAuthentication is result,errorCode:{},errorMessage:{}",
            setResponse.getErrorCode(), 
            setResponse.getErrorMessage()
        );

        return setResponse;
    }

    /**
     * register on the chain as an authoritative body.
     * 
     * @param authorityName the name of the issue
     * @return true is success, false is failure
     */
    @Override
    public ResponseData<Boolean> registerAuthorityIssuer(String issuer, String authorityName) {

        AuthorityIssuer authorityIssuerResult = new AuthorityIssuer();
        authorityIssuerResult.setWeId(issuer);
        authorityIssuerResult.setName(authorityName);
        authorityIssuerResult.setAccValue("0");

        RegisterAuthorityIssuerArgs registerAuthorityIssuerArgs = new RegisterAuthorityIssuerArgs();
        registerAuthorityIssuerArgs.setAuthorityIssuer(authorityIssuerResult);
        registerAuthorityIssuerArgs.setWeIdPrivateKey(new WeIdPrivateKey());

        String privKey = FileUtil.getDataByPath(PrivateKeyUtil.SDK_PRIVKEY_PATH);

        String privateKey = new BigInteger(privKey, 16).toString();
        registerAuthorityIssuerArgs.getWeIdPrivateKey().setPrivateKey(privateKey);

        ResponseData<Boolean> registResponse =
            authorityIssuerService.registerAuthorityIssuer(registerAuthorityIssuerArgs);
        LOGGER.info(
            "registerAuthorityIssuer is result,errorCode:{},errorMessage:{}",
            registResponse.getErrorCode(), 
            registResponse.getErrorMessage()
        );
        
        return registResponse;
    }

    /**
     * registered CPT.
     * 
     * @param publisher the weId of the publisher
     * @param privateKey the private key of the publisher
     * @param claim claim is CPT
     * @return returns cptBaseInfo
     */
    @Override
    public ResponseData<CptBaseInfo> registCpt(
        String publisher, 
        String privateKey, 
        Map<String, Object> claim) {

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(publisher);
        weIdAuthentication.setWeIdPrivateKey(new WeIdPrivateKey());
        weIdAuthentication.getWeIdPrivateKey().setPrivateKey(privateKey);
        
        CptMapArgs cptMapArgs = new CptMapArgs();
        cptMapArgs.setWeIdAuthentication(weIdAuthentication);
        cptMapArgs.setCptJsonSchema(claim);

        ResponseData<CptBaseInfo> response = cptService.registerCpt(cptMapArgs);
        LOGGER.info(
            "registerCpt is result,errorCode:{},errorMessage:{}", 
            response.getErrorCode(),
            response.getErrorMessage()
        );
        
        return response;
    }

    /**
     * create credential.
     * 
     * @param cptId the cptId of CPT 
     * @param issuer the weId of issue
     * @param privateKey the private key of issuer
     * @param claimDate the data of claim
     * @return returns credential
     */
    @Override
    public ResponseData<CredentialWrapper> createCredential(
        Integer cptId, 
        String issuer,
        String privateKey,
        Map<String, Object> claimDate) {

        CreateCredentialArgs registerCptArgs = new CreateCredentialArgs();
        registerCptArgs.setCptId(cptId);
        registerCptArgs.setIssuer(issuer);
        registerCptArgs.setWeIdPrivateKey(new WeIdPrivateKey());
        registerCptArgs.getWeIdPrivateKey().setPrivateKey(privateKey);
        registerCptArgs.setClaim(claimDate);
        // the validity period is 360 days
        registerCptArgs
            .setExpirationDate(System.currentTimeMillis() + EXPIRATION_DATE);

        ResponseData<CredentialWrapper> response = 
            credentialService.createCredential(registerCptArgs);
        LOGGER.info(
            "createCredential is result,errorCode:{},errorMessage:{}",
            response.getErrorCode(), 
            response.getErrorMessage()
        );
        
        return response;
    }

    /**
     * verify credential.
     * 
     * @param credentialJson credentials in JSON format
     * @return returns the result of verify
     */
    @Override
    public ResponseData<Boolean> verifyCredential(String credentialJson) {

        ResponseData<Boolean> verifyResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        Credential credential = null;

        try {
            credential = objectMapper.readValue(credentialJson, Credential.class);
        } catch (IOException e) {
            LOGGER.error("resolve credentialJson error.", e);
            verifyResponse = new ResponseData<Boolean>(false, ErrorCode.CREDENTIAL_ERROR);
            return verifyResponse;
        }

        verifyResponse = credentialService.verify(credential);
        LOGGER.info(
            "verifyCredential is result,errorCode:{},errorMessage:{}",
            verifyResponse.getErrorCode(), 
            verifyResponse.getErrorMessage()
        );
        
        return verifyResponse;
    }
}
