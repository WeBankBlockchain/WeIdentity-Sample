
package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.common.model.CreateEvidenceModel;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "存证其他相关接口。",
    tags = {"其他相关接口-Evidence"})
public class DemoOtherEvidenceController {

    @Autowired
    private DemoOtherService demoOtherService;

    @ApiOperation(value = "将传入Object计算Hash值生成存证上链，返回存证地址。传入的私钥将会成为链上存证的签名方。此签名方和凭证的Issuer可以不是同一方。"
        + "当传入的object为null时，则会创建一个空的存证并返回其地址，空存证中仅包含签名方，不含Hash值。"
        + "可以随后调用SetHashValue()方法，为空存证添加Hash值和签名。")
    @PostMapping("/step1/createEvidence")
    public ResponseData<String> createEvidence(
        @ApiParam(name = "credentialModel", value = "电子凭证模板")
        @RequestBody CreateEvidenceModel createEvidenceModel) {
        return demoOtherService.createEvidence(createEvidenceModel);
    }


    @ApiOperation(value = "根据传入的凭证存证Hash，在链上查找凭证存证信息。")
    @PostMapping("/step2/getEvidence")
    public ResponseData<EvidenceInfo> getEvidence(
        @ApiParam(
            name = "evidenceAddress",
            value = "凭证存证Hash", 
            example = "0x788bfde9ad99376673ed46847294e9b85872804573ed46847294e9b858728045"
        )
        @RequestParam(value = "evidenceAddress") String evidenceAddress) {

        return demoOtherService.getEvidence(evidenceAddress);
    }
}
