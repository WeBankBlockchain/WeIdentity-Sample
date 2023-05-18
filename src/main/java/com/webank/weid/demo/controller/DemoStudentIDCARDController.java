
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.demo.common.model.CreateEvidenceModel;
import com.webank.weid.demo.common.model.CreateStudentIDCARDModel;
import com.webank.weid.demo.common.model.StudentIDCARDModel;
import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.util.DataToolUtils;

/**
 * Demo Controller.
 *
 * @author zouheliang2011
 */
@RestController
@Api(description = "Credential发行者及Credential核验者的使用demo"
        + "本案例会将学校发行一个学生证，学生本人保管凭证,并出示凭证给机构,机构验证学生证，并确认学生证是否过期等场景",
    tags = {"学生证上链，及查验学生证凭证有效性及是否被撤销"})
public class DemoStudentIDCARDController {

    private static final Logger logger = LoggerFactory.getLogger(DemoStudentIDCARDController.class);

    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoOtherService demoOtherService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "学校注册上链：学生证场景，创建WeId")
    @PostMapping("/step1/studentidcard/createWeId")
    public ResponseData<CreateWeIdDataResult> createWeId() {
        return demoService.createWeId();
    }

    /**
     * institutional publication of CPT.
     * claim is a JSON object
     * @return returns CptBaseInfo
     */
    @ApiOperation(value = "学校定义学生证模板：注册CPT")
    @PostMapping("/step2/studentidcard/registCpt")
    public ResponseData<CptBaseInfo> registCpt(
        @ApiParam(name = "studentIDCARDModel", value = "学校定义学生证模板 CPT模板")
        @RequestBody StudentIDCARDModel studentIDCARDModel) {

        ResponseData<CptBaseInfo> response;
        try {
            if (null == studentIDCARDModel) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            String publisher = studentIDCARDModel.getPublisher();
            String credentialInfo = DataToolUtils.mapToCompactJson(studentIDCARDModel.getCredentialInfo());

            // get the private key from the file according to weId.
            String privateKey
                = PrivateKeyUtil.getPrivateKeyByWeId(PrivateKeyUtil.KEY_DIR, publisher);
            logger.info("param,publisher:{},privateKey:{},claim:{}", publisher, privateKey, credentialInfo);

            // converting claim in JSON format to map.
            Map<String, Object> credentialInfoMap = new HashMap<String, Object>();
            credentialInfoMap = 
                (Map<String, Object>) DataToolUtils.deserialize(
                    credentialInfo,
                    credentialInfoMap.getClass()
                );

            // call method to register CPT on the chain.
            response = demoService.registCpt(publisher, privateKey, credentialInfoMap);
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
    @ApiOperation(value = "颁发学生证电子凭证")
    @PostMapping("/step3/studentidcard/createCredential")
    public ResponseData<CredentialPojo> createCredential(
        @ApiParam(name = "createIDCARDModel", value = "创建电子凭证")
        @RequestBody CreateStudentIDCARDModel createIDCARDModel) {

        ResponseData<CredentialPojo> response;
        try {

            if (null == createIDCARDModel) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            // getting cptId data.
            Integer cptId = createIDCARDModel.getCptId();
            // getting issuer data.
            String issuer = createIDCARDModel.getIssuer();
            // getting claimData data.
            String claimData = DataToolUtils.mapToCompactJson(createIDCARDModel.getClaimData());

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

     

   

    @ApiOperation(value = "存证:将传入颁发学生证电子凭证的返回结果(可以自定义其它字段,比如是注册还是注销),即先计算学生证电子Hash值,然后生成存证上链，返回存证地址。存证平台的签名可以不是学生证的颁发机构,可以是第三方公证处能有发布存证能力在链上的平台"
        + "当传入的object为null时，则会创建一个空的存证并返回其地址，空存证中仅包含签名方，不含Hash值。"
        + "可以随后调用SetHashValue()方法，为空存证添加Hash值和签名。")
    @PostMapping("/step4/studentidcard/createEvidence")
    public ResponseData<String> createEvidence(
        @ApiParam(name = "credentialModel", value = "电子凭证模板")
        @RequestBody CreateEvidenceModel createEvidenceModel) {
        return demoOtherService.createEvidence(createEvidenceModel);
    }


    @ApiOperation(value = "存证查询:根据传入的凭证存证Hash，在链上查找凭证存证信息。业务场景上存证是为了做一些链下签名数据的撤销操作证明,例如学生证签名虽然验证成功,"
         +"学校依旧可以撤销学生证,比如休学,毕业等场景,需要对已经发出的签名凭证进行存证,保存凭证的状态,或者只将无效的数据上存证.由业务方决定")
    @PostMapping("/step5/studentidcard/getEvidence")
    public ResponseData<EvidenceInfo> getEvidence(
        @ApiParam(
            name = "evidenceAddress",
            value = "凭证存证Hash", 
            example = "0x788bfde9ad99376673ed46847294e9b85872804573ed46847294e9b858728045"
        )
        @RequestParam(value = "evidenceAddress") String evidenceAddress) {

        return demoOtherService.getEvidence(evidenceAddress);
    }

    
    @ApiOperation(value = "验证凭证是否正确,业务上验证完成后还需要查询存证是否由数据,确认凭证是否已经被撤销了")
    @PostMapping("/step6/studentidcard/verifyCredential")
    public ResponseData<Boolean> verifyCredential(
        @ApiParam(name = "verifyCredentialModel", value = "验证电子凭证")
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
