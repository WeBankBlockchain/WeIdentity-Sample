
package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.common.model.CreateCredentialPojoModel;
import com.webank.weid.demo.common.model.CreatePresentationModel;
import com.webank.weid.demo.common.model.CreatePresentationPolicyEModel;
import com.webank.weid.demo.common.model.CreateSelectiveCredentialModel;
import com.webank.weid.demo.common.model.CredentialPoJoAddSignature;
import com.webank.weid.demo.common.model.GetCredentialHashModel;
import com.webank.weid.demo.common.model.VerifyCredentialPoJoModel;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.PresentationE;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "电子凭证其他相关接口。",
    tags = {"其他相关接口-CredentialPoJo"})
public class DemoOtherCredentialPoJoController {

    @Autowired
    private DemoOtherService demoOtherService;


    @ApiOperation(value = "传入Credential信息生成Credential整体的Hash值，一般在生成Evidence时调用。")
    @PostMapping("/step1/createCredentialPoJo")
    public ResponseData<CredentialPojo> createCredentialPoJo(
        @ApiParam(name = "createCredentialModel", value = "电子凭证模板")
        @RequestBody CreateCredentialPojoModel createCredentialPojoModel) {

        return demoOtherService.createCredentialPoJo(createCredentialPojoModel);
    }

    @ApiOperation(value = "通过原始凭证和披漏策略，创建选择性披露的Credential。")
    @PostMapping("/step2/createSelectiveCredential")
    public ResponseData<CredentialPojo> createSelectiveCredential(
        @ApiParam(name = "createSelectiveCredentialModel", value = "选择性披露电子凭证模板")
        @RequestBody CreateSelectiveCredentialModel createSelectiveCredentialModel) {

        return demoOtherService.createSelectiveCredential(createSelectiveCredentialModel);
    }

    @ApiOperation(value = "验证电子凭证。")
    @PostMapping("/step3/verifyEvidence")
    public ResponseData<Boolean> verify(
        @ApiParam(name = "verifyCredentialPoJoModel", value = "验证电子凭证模板")
        @RequestBody VerifyCredentialPoJoModel verifyCredentialPoJoModel) {

        return demoOtherService.verify(verifyCredentialPoJoModel);
    }

    @ApiOperation(value = "创建PresentationPolicyE。")
    @PostMapping("/step4/createPresentationPolicyE")
    public ResponseData<PresentationPolicyE> createPresentationPolicyE(
        @ApiParam(name = "createPresentationModel", value = "创建Presentation模板")
        @RequestBody CreatePresentationPolicyEModel createPresentationPolicyEModel) {

        return demoOtherService.createPresentationPolicyE(createPresentationPolicyEModel);
    }

    @ApiOperation(value = "创建Presentation。")
    @PostMapping("/step5/createPresentation")
    public ResponseData<PresentationE> createPresentation(
        @ApiParam(name = "createPresentationModel", value = "创建Presentation模板")
        @RequestBody CreatePresentationModel createPresentationModel) {

        return demoOtherService.createPresentation(createPresentationModel);
    }

    @ApiOperation(value = "传入CredentialPojo信息生成CredentialPojo整体的Hash值，一般在生成Evidence时调用。")
    @PostMapping("/step6/getCredentialPoJoHash")
    public ResponseData<String> getCredentialPoJoHash(
        @ApiParam(name = "getCredentialHashModel", value = "CredentialPojo模板")
        @RequestBody GetCredentialHashModel getCredentialHashModel) {

        return demoOtherService.getCredentialPoJoHash(getCredentialHashModel);
    }

    @ApiOperation(value = "多签，在原凭证列表的基础上，创建包裹成一个新的多签凭证，由传入的私钥所签名。"
        + "此凭证的CPT为一个固定值。在验证一个多签凭证时，会迭代验证其包裹的所有子凭证。本接口不支持创建选择性披露的多签凭证。")
    @PostMapping("/step7/addSignatureCredentialPojo")
    public ResponseData<CredentialPojo> addSignatureCredentialPojo(
        @ApiParam(name = "credentialPoJoAddSignature", value = "CredentialPojo加签模板")
        @RequestBody CredentialPoJoAddSignature credentialPoJoAddSignature) {

        return demoOtherService.addSignatureCredentialPojo(credentialPoJoAddSignature);
    }
}
