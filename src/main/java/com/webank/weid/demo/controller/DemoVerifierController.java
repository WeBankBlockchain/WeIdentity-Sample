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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.response.ResponseData;
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
