
package com.webank.weid.demo.command;

import com.webank.weid.kit.transportation.TransportationFactory;
import com.webank.weid.kit.transportation.entity.TransportationType;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.CredentialPojoList;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;
import com.webank.weid.service.rpc.CredentialPojoService;
import com.webank.weid.service.rpc.WeIdService;

public class TransportationVerifier {

    /**
     * verifier程序入口.
     * @param args 参数
     */
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        // TODO Auto-generated method stub
        // 创建weid
        WeIdService weidService = new WeIdServiceImpl();
        CreateWeIdDataResult result = weidService.createWeId().getResult();
        System.out.println(result);
        // verifier WeId信息
        String weId = "did:weid:101:0xa7af4461084b76aecfd9361a49e21cc34e6c4074";
        WeIdPrivateKey privateKey = new WeIdPrivateKey();
        privateKey.setPrivateKey(
            "89119831904759961853601218812211426488644695651785968136169259389312366556150");
        
        // 构造verifier的身份信息
        WeIdAuthentication weIdAuthentication = 
            new WeIdAuthentication(weId, privateKey.getPrivateKey());

        com.webank.weid.kit.protocol.response.ResponseData<CredentialPojoList> deserialize =
            TransportationFactory.build(TransportationType.QR_CODE)
                .deserialize(
                      weIdAuthentication, 
                     "2|1000orgB|993ecb3ffdbd47e5a3e2e7a798060ef1",//issuer通过serialize生成的条码编码
                     CredentialPojoList.class
                 );
        System.out.println("根据条码编码获取到的凭证:");
        System.out.println(deserialize);
        
        // 验证获取到凭证
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        CredentialPojoList credentialPojoList = deserialize.getResult();
        for (CredentialPojo credentialPojo2 : credentialPojoList) {
            ResponseData<Boolean> verify = 
                credentialPojoService.verify(credentialPojo2.getIssuer(), credentialPojo2);
            System.out.println("凭证验证结果:");
            System.out.println(verify);
        }
    }
}
