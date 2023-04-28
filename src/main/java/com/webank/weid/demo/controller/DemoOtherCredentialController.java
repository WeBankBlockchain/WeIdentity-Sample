
package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "电子凭证其他相关接口。",
    tags = {"其他相关接口-Credential"})
public class DemoOtherCredentialController {

    @Autowired
    private DemoOtherService demoOtherService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "传入Credential信息生成Credential整体的Hash值，一般在生成Evidence时调用。")
    @PostMapping("/step1/credential/getCredentialPoJoHash")
    public ResponseData<String> getCredentialHash(
        @ApiParam(name = "credentialModel", value = "电子凭证模板")
        @RequestBody VerifyCredentialModel verifyCredentialModel) {

        return demoOtherService.getCredentialHash(verifyCredentialModel);
    }
}
