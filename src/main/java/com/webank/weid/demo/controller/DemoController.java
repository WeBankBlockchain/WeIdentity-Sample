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

package com.webank.weid.demo.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import org.apache.commons.lang3.StringUtils;
import org.bcos.web3j.crypto.ECKeyPair;
import org.bcos.web3j.crypto.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.common.dto.PasswordKey;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.common.util.PropertiesUtils;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.util.DataToolUtils;

/**
 * Demo Controller.
 *
 * @author v_wbgyang
 */
@RestController
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DemoService demoService;

    /**
     * this directory is used to store private keys, but keep your private
     * keys properly in your project.
     */
    private static final String KEY_DIR = PropertiesUtils.getProperty("weid.keys.dir");

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @PostMapping("/createWeId")
    public ResponseData<CreateWeIdDataResult> createWeId() {

        // create weId and set related properties.
        ResponseData<CreateWeIdDataResult> response = demoService.createWeIdWithSetAttr();

        // if weId is created successfully, save its private key.
        if (response.getErrorCode().intValue() == ErrorCode.SUCCESS.getCode()) {
            PrivateKeyUtil.savePrivateKey(
                KEY_DIR, 
                response.getResult().getWeId(),
                response.getResult().getUserWeIdPrivateKey().getPrivateKey()
            );
        }

        /*
         *  private keys are not allowed to be transmitted over http, so this place
         *  annotates the return of private keys to avoid misuse.
         */
        response.getResult().setUserWeIdPrivateKey(null);
        return response;
    }

    /**
     * create public and private keys.
     * note this method as a demonstration of how to create public and private
     * keys by code itself. private keys do not allow network transmission.
     * please keep the private keys you create properly.
     * 
     * @return returns public and private keys
     */
    public PasswordKey createKeys() {

        PasswordKey passwordKey = new PasswordKey();
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            String publicKey = String.valueOf(keyPair.getPublicKey());
            String privateKey = String.valueOf(keyPair.getPrivateKey());
            passwordKey.setPrivateKey(privateKey);
            passwordKey.setPublicKey(publicKey);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("createKeys error.", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("createKeys error.", e);
        } catch (NoSuchProviderException e) {
            logger.error("createKeys error.", e);
        }
        return passwordKey;
    }

    /**
     * pass in your own public and private keys, create weId and set related properties.
     * note that as a demonstration. if weId is created based on public and private keys
     * to avoid network transmission of private keys. mapping is not added here to avoid
     * misuse.
     *
     */
    public ResponseData<String> createWeIdByKeys(@RequestBody Map<String, String> paramMap) {

        String publicKey = paramMap.get("publicKey");
        String privateKey = paramMap.get("privateKey");
        logger.info("param,publicKey:{},privateKey:{}", publicKey, privateKey);

        // create weId and set related properties.
        ResponseData<String> response = demoService.createWeIdAndSetAttr(publicKey, privateKey);

        // if weId is created successfully, save its private key.
        if (response.getErrorCode().intValue() == ErrorCode.SUCCESS.getCode()) {
            PrivateKeyUtil.savePrivateKey(KEY_DIR, response.getResult(), privateKey);
        }
        return response;
    }

    /**
     * registered on the chain of institutions as authoritative bodies.
     *
     * @return true is success, false is failure.
     */
    @PostMapping("/registerAuthorityIssuer")
    public ResponseData<Boolean> registerAuthorityIssuer(
        @RequestBody Map<String, String> paramMap) {

        String issuer = paramMap.get("issuer");
        String authorityName = paramMap.get("authorityName");

        logger.info("param,issuer:{},authorityName:{}", issuer, authorityName);

        // call method registered as authority.
        return demoService.registerAuthorityIssuer(issuer, authorityName);
    }

    /**
     * institutional publication of CPT.
     * claim is a JSON object
     * @return returns CptBaseInfo
     */
    @PostMapping("/registCpt")
    public ResponseData<CptBaseInfo> registCpt(@RequestBody String jsonStr) {

        ResponseData<CptBaseInfo> response = null;
        try {

            // converting request data in JSON format into JsonNode.
            JsonNode jsonNode = JsonLoader.fromString(jsonStr);

            // getting publisher data.
            JsonNode publisherNode = jsonNode.get("publisher");
            String publisher = null;
            if (publisherNode != null) {
                publisher = publisherNode.textValue();
            }

            // getting claim data.
            JsonNode claimNode = jsonNode.get("claim");
            String claim = null;
            if (claimNode != null) {
                claim = claimNode.toString();
            }

            // get the private key from the file according to weId.
            String privateKey = PrivateKeyUtil.getPrivateKeyByWeId(KEY_DIR, publisher);
            logger.info("param,publisher:{},privateKey:{},claim:{}", publisher, privateKey, claim);

            // converting claim in JSON format to map.
            Map<String, Object> claimMap = new HashMap<String, Object>();
            claimMap = 
                (Map<String, Object>) DataToolUtils.deserialize(
                    claim,
                    claimMap.getClass()
                );

            // call method to register CPT on the chain.
            response = demoService.registCpt(publisher, privateKey, claimMap);
        } catch (Exception e) {
            logger.error("registCpt error", e);
            response = new ResponseData<CptBaseInfo>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
        return response;
    }

    /**
     * institutional publication of Credential.
     *
     * @return returns  credential
     * @throws IOException  it's possible to throw an exception
     */
    @PostMapping("/createCredential")
    public ResponseData<CredentialWrapper> createCredential(@RequestBody String jsonStr) {

        ResponseData<CredentialWrapper> response = null;
        try {

            // converting request data in JSON format into JsonNode.
            JsonNode jsonNode = JsonLoader.fromString(jsonStr);

            // getting cptId data.
            JsonNode cptIdNode = jsonNode.get("cptId");
            Integer cptId = null;
            if (cptIdNode != null && StringUtils.isNotBlank(cptIdNode.textValue())) {
                cptId = Integer.parseInt(cptIdNode.textValue());
            }

            // getting issuer data.
            JsonNode issuerNode = jsonNode.get("issuer");
            String issuer = null;
            if (issuerNode != null) {
                issuer = issuerNode.textValue();
            }

            // getting claimData data.
            JsonNode claimDataNode = jsonNode.get("claimData");
            String claimData = null;
            if (claimDataNode != null) {
                claimData = claimDataNode.toString();
            }

            // get the private key from the file according to weId.
            String privateKey = PrivateKeyUtil.getPrivateKeyByWeId(KEY_DIR, issuer);
            logger.info(
                "param,cptId:{},issuer:{},privateKey:{},claimData:{}", 
                cptId, 
                issuer,
                privateKey, 
                claimData
            );

            // converting claimData in JSON format to map.
            Map<String, Object> claimDataMap = new HashMap<String, Object>();
            claimDataMap = 
                (Map<String, Object>) DataToolUtils.deserialize(
                    claimData,
                    claimDataMap.getClass()
                );

            // call method to create credentials.
            response = demoService.createCredential(cptId, issuer, privateKey, claimDataMap);
        } catch (IOException e) {
            logger.error("createCredential error", e);
            response = new ResponseData<CredentialWrapper>(null, ErrorCode.CREDENTIAL_ERROR);
        }
        return response;
    }

    /**
     * verify Credential.
     *
     * @param credentialJson credential in JSON format
     * @return true is success, false is failure
     */
    @PostMapping("/verifyCredential")
    public ResponseData<Boolean> verifyCredential(@RequestBody String credentialJson) {

        logger.info("param,credentialJson:{}", credentialJson);

        // call method to verify credential.
        return demoService.verifyCredential(credentialJson);
    }
}
