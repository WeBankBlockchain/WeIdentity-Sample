
package com.webank.weid.demo.command;

import java.util.HashMap;
import java.util.Map;

import com.webank.weid.constant.CredentialType;
import com.webank.weid.constant.ProcessingMode;
import com.webank.weid.kit.crypto.CryptoServiceFactory;
import com.webank.weid.kit.crypto.params.CryptoType;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.CredentialPojoList;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.EvidenceServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;
import com.webank.weid.service.rpc.CredentialPojoService;
import com.webank.weid.service.rpc.EvidenceService;
import com.webank.weid.service.rpc.WeIdService;

public class MultiGroupEvidenceSample {

    static WeIdService weidService = new WeIdServiceImpl();

    static CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();

    static EvidenceService evidenceService = new EvidenceServiceImpl(ProcessingMode.IMMEDIATE, "2");

    /**
     * Demo过程说明
     * 1. 创建weid
     * 2. 创建lite Credential1 创建lite Credential2
     * 3. 将两个Credential放入CredentialPojoList中
     * 4. CredentialPojoList转json
     * 5. CredentialPojoList json 加密
     * 6. CredentialPojoList json 解密
     * 7. CredentialPojoList json 转 CredentialPojoList
     * 8. 验证Credential
     * 9. 根据Credential创建Evidence
     * 10. 获取存证数据
     * 11. 验证存在数据
     * 
     * @param args 入参
     */
    public static void main(String[] args) {
        // 1. 创建weid
        ResponseData<CreateWeIdDataResult> createWeIdRes = weidService.createWeId();
        System.out.println("创建weid结果：" + createWeIdRes);
        CreateWeIdDataResult createWeId = createWeIdRes.getResult();
        // 省略返回code判断
       
        // 2. 创建lite Credential
        ResponseData<WeIdDocument> weIdDocumentRes = 
            weidService.getWeIdDocument(createWeId.getWeId());
        String publicKeyId = createWeIdRes.getResult().getUserWeIdPublicKey().getPublicKey();

        // 构造WeIdAuthentication
        WeIdAuthentication weIdAuthentication = buildWeIdAuthority(createWeId, publicKeyId);
        CreateCredentialPojoArgs<Map<String, Object>> createArgs = 
            buildCreateCredentialPojoArgs(weIdAuthentication); 
        ResponseData<CredentialPojo> createCredential1 =
            credentialPojoService.createCredential(createArgs);
        System.out.println("lite Credential1 创建结果:" + createCredential1);
        ResponseData<CredentialPojo> createCredential2 = 
            credentialPojoService.createCredential(createArgs);
        System.out.println("lite Credential2 创建结果:" + createCredential2);
        // 省略返回code判断

        // 3.将CredentialPojo放入CredentialPojoList中
        CredentialPojoList credentialPojoList = new CredentialPojoList();
        credentialPojoList.add(createCredential1.getResult());
        credentialPojoList.add(createCredential2.getResult());
        
        // 4. CredentialPojoList转json
        String credentialListJson = credentialPojoList.toJson();
        System.out.println("CredentialPojoList 转Json 结果:" + credentialListJson);
        
        // 5. 加密Credential Json
        String encrypt = CryptoServiceFactory.getCryptoService(CryptoType.ECIES).encrypt(
            credentialListJson, createWeId.getUserWeIdPublicKey().getPublicKey());
        System.out.println("credentialList Json 加密结果:" + encrypt);
        
        // 6. 解密 Credential Json
        String decrypt = CryptoServiceFactory.getCryptoService(CryptoType.ECIES).decrypt(
            encrypt, createWeId.getUserWeIdPrivateKey().getPrivateKey());
        System.out.println("credentialList Json 解密结果:" + decrypt);
        
        // 7. Credential Json 转Credential
        CredentialPojoList credential = CredentialPojoList.fromJson(credentialListJson);
        System.out.println("解密后的credentialList Json 转 credentialList结果：" + credential);
        
        for (CredentialPojo credentialPojo : credential) {
            // 8. 验证Credential
            ResponseData<Boolean> verifyRes = 
                credentialPojoService.verify(createWeId.getWeId(), credentialPojo);
            System.out.println("Credential 验证结果：" + verifyRes);
            // 省略返回code判断
            
            // 9. 创建Evidence
            ResponseData<String> createEvidence = 
                evidenceService.createEvidence(credentialPojo, createWeId.getUserWeIdPrivateKey());
            System.out.println("存证创建结果:" + createEvidence);
            // 省略返回code判断
            
            // 10. 获取存证
            ResponseData<EvidenceInfo> evidenceRes = 
                evidenceService.getEvidence(createEvidence.getResult());
            System.out.println("获取存证结果：" + evidenceRes);
            // 省略返回code判断
            
            // 11. 验证存证
            //  ResponseData<Boolean> verifySigner =
            //      evidenceService.verifySigner(evidenceRes.getResult(), createWeId.getWeId());
            ResponseData<Boolean> verifySigner = evidenceService.verifySigner(
                credentialPojo, evidenceRes.getResult(), createWeId.getWeId());
            System.out.println("存证验证结果:" + verifySigner);
            // 省略返回code判断
        }
    }
    
    /**
     * 构建凭证创建参数.
     * @param weIdAuthentication weId身份信息
     * @return 返回凭证创建参数
     */
    public static CreateCredentialPojoArgs<Map<String, Object>> buildCreateCredentialPojoArgs(
        WeIdAuthentication weIdAuthentication
    ) {
        CreateCredentialPojoArgs<Map<String, Object>> createArgs = new CreateCredentialPojoArgs<>();
        createArgs.setIssuer(weIdAuthentication.getWeId());
        createArgs.setExpirationDate(
            System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        createArgs.setWeIdAuthentication(weIdAuthentication);
        Map<String, Object> claimMap = new HashMap<String, Object>();
        claimMap.put("name", "zhang san");
        claimMap.put("gender", "F");
        claimMap.put("age", 23);
        createArgs.setClaim(claimMap);
        createArgs.setCptId(1000);
        createArgs.setType(CredentialType.LITE1);
        return createArgs;
    }

    /**
     * build weId authority.
     */
    public static WeIdAuthentication buildWeIdAuthority(
        CreateWeIdDataResult createWeId, 
        String publicKeyId
    ) {
        return new WeIdAuthentication(
            createWeId.getWeId(), 
            createWeId.getUserWeIdPrivateKey().getPrivateKey(),
            publicKeyId
        );
    }
}