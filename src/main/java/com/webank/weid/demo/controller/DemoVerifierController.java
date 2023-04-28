
package com.webank.weid.demo.controller;

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
import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.util.DataToolUtils;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "Verifier: Credential的使用者。"
        + "会验证实体对WeIdentity DID的所有权，其次在链上验证Credential的真实性，以便处理相关业务。",
    tags = {"Verifier相关接口"}, position = 0)
public class DemoVerifierController {

    private static final Logger logger = LoggerFactory.getLogger(DemoVerifierController.class);

    @Autowired
    private DemoService demoService;
    
    /**
     * verifyEvidence Credential.
     *
     * @param verifyCredentialModel credential in JSON format
     * @return true is success, false is failure
     */
    @ApiOperation(value = "验证凭证是否正确")
    @PostMapping("/step1/verifyCredential")
    public ResponseData<Boolean> verifyCredential(
        @ApiParam(name = "verifyCredentialModel", value = "验证电子凭证模板")
        @RequestBody VerifyCredentialModel verifyCredentialModel) {

        logger.info("verifyCredentialModel:{}", verifyCredentialModel);

        if (null == verifyCredentialModel) {
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }
        // call method to verifyEvidence credential.
        try {
            return demoService.verifyCredential(
                DataToolUtils.mapToCompactJson(verifyCredentialModel.getCredential()));
        } catch (Exception e) {
            logger.error("verifyCredential error", e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }
}
