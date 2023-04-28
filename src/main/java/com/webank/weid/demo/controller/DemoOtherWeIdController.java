
package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.demo.common.model.CreateWeIdModel;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "WeId其他相关接口。",
    tags = {"其他相关接口-WeId"}, hidden = false)
public class DemoOtherWeIdController {

    @Autowired
    private DemoService demoService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "通过公私钥对创建WeId。")
    @PostMapping("/step1/createWeId")
    public ResponseData<String> createWeId(
        @ApiParam(name = "credentialModel", value = "公私钥对模板")
        @RequestBody CreateWeIdModel createWeIdModel) {

        if (null == createWeIdModel
            || StringUtils.isBlank(createWeIdModel.getPublicKey())
            || StringUtils.isBlank(createWeIdModel.getPrivateKey())) {
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }
        return demoService.createWeIdAndSetAttr(
            createWeIdModel.getPublicKey(),
            createWeIdModel.getPrivateKey());
    }
}
