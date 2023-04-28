
package com.webank.weid.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.demo.common.model.CptModel;
import com.webank.weid.demo.common.model.CreateCredentialModel;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.util.DataToolUtils;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "Issuer: Credential的发行者。"
        + "会验证实体对WeIdentity DID的所有权，其次发行实体相关的Credential。",
    tags = {"Issuer相关接口"})
public class DemoIssuerController {

    private static final Logger logger = LoggerFactory.getLogger(DemoIssuerController.class);

    @Autowired
    private DemoService demoService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "创建WeId")
    @PostMapping("/step1/issuer/createWeId")
    public ResponseData<CreateWeIdDataResult> createWeId() {
        return demoService.createWeId();
    }

    /**
     * institutional publication of CPT.
     * claim is a JSON object
     * @return returns CptBaseInfo
     */
    @ApiOperation(value = "注册CPT")
    @PostMapping("/step2/registCpt")
    public ResponseData<CptBaseInfo> registCpt(
        @ApiParam(name = "cptModel", value = "CPT模板")
        @RequestBody CptModel cptModel) {

        ResponseData<CptBaseInfo> response;
        try {
            if (null == cptModel) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            String publisher = cptModel.getPublisher();
            String claim = DataToolUtils.mapToCompactJson(cptModel.getClaim());

            // get the private key from the file according to weId.
            String privateKey
                = PrivateKeyUtil.getPrivateKeyByWeId(PrivateKeyUtil.KEY_DIR, publisher);
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
            logger.info("registCpt response: {}", DataToolUtils.objToJsonStrWithNoPretty(response));
            return response;
        } catch (Exception e) {
            logger.error("registCpt error", e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    /**
     * institutional publication of Credential.
     *
     * @return returns  credential
     * @throws IOException  it's possible to throw an exception
     */
    @ApiOperation(value = "创建电子凭证")
    @PostMapping("/step3/createCredential")
    public ResponseData<CredentialPojo> createCredential(
        @ApiParam(name = "createCredentialModel", value = "创建电子凭证模板")
        @RequestBody CreateCredentialModel createCredentialModel) {

        ResponseData<CredentialPojo> response;
        try {

            if (null == createCredentialModel) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            // getting cptId data.
            Integer cptId = createCredentialModel.getCptId();
            // getting issuer data.
            String issuer = createCredentialModel.getIssuer();
            // getting claimData data.
            String claimData = DataToolUtils.mapToCompactJson(createCredentialModel.getClaimData());

            // get the private key from the file according to weId.
            String privateKey = PrivateKeyUtil.getPrivateKeyByWeId(PrivateKeyUtil.KEY_DIR, issuer);
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
            logger.info("createCredential response: {}",
                DataToolUtils.objToJsonStrWithNoPretty(response));
            return response;
        } catch (Exception e) {
            logger.error("createCredential error", e);
            return new ResponseData<CredentialPojo>(null, ErrorCode.CREDENTIAL_ERROR);
        }
    }


}
