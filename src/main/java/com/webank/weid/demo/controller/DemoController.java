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
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
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
import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.demo.common.util.PropertiesUtils;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;

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
    private String keyDir = PropertiesUtils.getProperty("weid.keys.dir");

    /**
     * jsonSchema.
     */
    public static final String SCHEMA;

    static {
        // default jsonSchema template
        SCHEMA = FileUtil.getDataByPath("./claim/JsonSchema.json");
    }

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @PostMapping("/createWeId")
    public ResponseData<CreateWeIdDataResult> createWeId() {
        ResponseData<CreateWeIdDataResult> response = demoService.createWeIdWithSetAttr();

        if (response.getErrorCode().intValue() == ErrorCode.SUCCESS.getCode()) {
            FileUtil.savePrivateKey(
                keyDir, 
                response.getResult().getWeId(),
                response.getResult().getUserWeIdPrivateKey().getPrivateKey());
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

        ResponseData<String> response = demoService.createWeIdAndSetAttr(publicKey, privateKey);

        if (response.getErrorCode().intValue() == ErrorCode.SUCCESS.getCode()) {
            FileUtil.savePrivateKey(keyDir, response.getResult(), privateKey);
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
        return demoService.registerAuthorityIssuer(issuer, authorityName);
    }

    /**
     * institutional publication of CPT.
     * claim is a JSON object
     * @return returns CptBaseInfo
     */
    @PostMapping("/registCpt")
    public ResponseData<CptBaseInfo> registCpt(@RequestBody String jsonStr)
        throws IOException {
        
        JsonNode jsonNode = JsonLoader.fromString(jsonStr);
        String publisher = jsonNode.get("publisher").textValue();
        String claim = jsonNode.get("claim").toString();
        String privateKey = FileUtil.getPrivateKeyByWeId(keyDir, publisher);
        logger.info("param,publisher:{},privateKey:{},claim:{}", publisher, privateKey, claim);
        claim = this.getJsonSchema(claim);
        return demoService.registCpt(publisher, privateKey, claim);
    }

    /**
     * institutional publication of Credential.
     *
     * @return returns  credential
     * @throws IOException  it's possible to throw an exception
     */
    @PostMapping("/createCredential")
    public ResponseData<Credential> createCredential(@RequestBody String jsonStr)
        throws IOException {
        
        JsonNode jsonNode = JsonLoader.fromString(jsonStr);
        String cptIdStr = jsonNode.get("cptId").textValue();
        String issuer = jsonNode.get("issuer").textValue();
        String claimData = jsonNode.get("claimData").toString();
        Integer cptId = Integer.parseInt(cptIdStr);

        String privateKey = FileUtil.getPrivateKeyByWeId(keyDir, issuer);
        logger.info("param,cptId:{},issuer:{},privateKey:{},claimData:{}", 
            cptId, 
            issuer,
            privateKey, 
            claimData);
        
        return demoService.createCredential(cptId, issuer, privateKey, claimData);
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
        return demoService.verifyCredential(credentialJson);
    }

    /**
     * converting the user's incoming claim into the required jsonSchema.
     *
     * @param claim cpt
     * @return schema jsonSchema
     */
    @SuppressWarnings("deprecation")
    private String getJsonSchema(String claim) throws IOException {
        JsonNode jsonNode = JsonLoader.fromString(SCHEMA);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("properties", JsonLoader.fromString(claim));
        return jsonNode.toString();
    }
}
