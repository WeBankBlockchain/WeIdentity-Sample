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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.common.model.AddSignatureModel;
import com.webank.weid.demo.common.model.CreateEvidenceModel;
import com.webank.weid.demo.common.model.SetHashValueModel;
import com.webank.weid.demo.common.model.VerifyEvidenceModel;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.response.ResponseData;

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


    @ApiOperation(value = "根据传入的凭证存证地址，在链上查找凭证存证信息。")
    @PostMapping("/step2/getEvidence")
    public ResponseData<EvidenceInfo> getEvidence(
        @ApiParam(name = "evidenceAddress",
            value = "凭证存证地址", example = "0x788bfde9ad99376673ed46847294e9b858728045")
        @RequestParam(value = "evidenceAddress") String evidenceAddress) {

        return demoOtherService.getEvidence(evidenceAddress);
    }

    @ApiOperation(value = "对传入的Object及链上地址，加一个签名存入链上的存证。要求：传入的签名方必须隶属于在创建存证时传入多个签名方的WeID之一。")
    @PostMapping("/step3/addSignatureCredentialPojo")
    public ResponseData<Boolean> addSignature(
        @ApiParam(name = "addSignatureCredentialPojo", value = "加签模板")
        @RequestBody AddSignatureModel addSignatureModel) {
        return demoOtherService.addSignature(addSignatureModel);
    }

    @ApiOperation(value = "根据传入的Object计算存证Hash值和链上值对比，验证其是否遭到篡改。"
        + "当存证包含多个签名时，将会依次验证每个签名，必须确实由签名者列表中的某个WeID所签发才算验证成功。")
    @PostMapping("/step4/verifyEvidence")
    public ResponseData<Boolean> verifyEvidence(
        @ApiParam(name = "verifyEvidenceModel", value = "存证验签模板")
        @RequestBody VerifyEvidenceModel verifyEvidenceModel) {
        return demoOtherService.verifyEvidence(verifyEvidenceModel);
    }

    @ApiOperation(value = "对指定的空存证地址，将其链上的Hash值设定为所传入的Hash值。"
        + "传入的私钥必须是创建存证时所声明的签名者之一。注意：当存证非空时，接口将返回失败。")
    @PostMapping("/step5/setHashValue")
    public ResponseData<Boolean> setHashValue(
        @ApiParam(name = "setHashValueModel", value = "对指定的空存证地址设值模板")
        @RequestBody SetHashValueModel setHashValueModel) {
        return demoOtherService.setHashValue(setHashValueModel);
    }
}
