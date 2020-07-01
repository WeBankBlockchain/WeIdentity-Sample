/*
 *       Copyright© (2019-2020) WeBank Co., Ltd.
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

package com.webank.weid.demo.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webank.weid.constant.JsonSchemaConstant;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.CredentialPojoList;
import com.webank.weid.protocol.base.PublicKeyProperty;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.base.WeIdPublicKey;
import com.webank.weid.protocol.request.CptMapArgs;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CptService;
import com.webank.weid.rpc.CredentialPojoService;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.impl.CptServiceImpl;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;
import com.webank.weid.suite.api.transportation.TransportationFactory;
import com.webank.weid.suite.api.transportation.params.EncodeType;
import com.webank.weid.suite.api.transportation.params.ProtocolProperty;
import com.webank.weid.suite.api.transportation.params.TransMode;
import com.webank.weid.suite.api.transportation.params.TransportationType;

public class TransportaionIssuer {
    
    static WeIdService weidService = new WeIdServiceImpl();
    static CptService cptService = new CptServiceImpl();
    static CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
    
    /**
     * issuer main.
     * @param args 入参
     * @throws IOException IO异常
     */
    public static void main(String[] args) throws IOException {
        
        // 创建weid
        CreateWeIdDataResult result = weidService.createWeId().getResult();
        ResponseData<WeIdDocument> weIdDocumentRes = weidService.getWeIdDocument(result.getWeId());
        String publicKeyId = null;
        for (PublicKeyProperty publicKey : weIdDocumentRes.getResult().getPublicKey()) {
            if (publicKey.getOwner().equals(result.getWeId())) {
                publicKeyId = publicKey.getId();
                break;
            }
        }
        // 构造WeIdAuthentication
        WeIdAuthentication weIdAuthentication = buildWeIdAuthority(result, publicKeyId);
        
        // 注册cpt
        CptMapArgs registerCptArgs = buildCptArgs(weIdAuthentication);
        CptBaseInfo cptBaseInfo = cptService.registerCpt(registerCptArgs).getResult();
        
        // 创建凭证
        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs =
            buildCreateCredentialPojoArgs(weIdAuthentication);
        createCredentialPojoArgs.setCptId(cptBaseInfo.getCptId());
        ResponseData<CredentialPojo> credentialPojoRes =
             credentialPojoService.createCredential(createCredentialPojoArgs);
        System.out.println("创建凭证结果:");
        System.out.println(credentialPojoRes);
        
        WeIdPublicKey weidPublicKey = new WeIdPublicKey();
        weidPublicKey.setPublicKey(result.getUserWeIdPublicKey().getPublicKey());
        ResponseData<Boolean> verify2 = credentialPojoService.verify(
            weidPublicKey, credentialPojoRes.getResult());
        System.out.println("根据公钥验证结果：" + verify2);
        
        
        // 凭证放入CredentialPojoList中
        CredentialPojoList list = new CredentialPojoList();
        list.add(credentialPojoRes.getResult());
        
        // 指定可以解析二维码的机构白名单
        List<String> verifierWeIdList = new ArrayList<String>();
        verifierWeIdList.add("did:weid:101:0xa7af4461084b76aecfd9361a49e21cc34e6c4074");
        verifierWeIdList.add(result.getWeId());
        
        // 调用序列化接口，生成条码编码
        // 如果序列化Presentation 将list修改成Presentation对象
        ResponseData<String> serialize = 
            TransportationFactory.build(TransportationType.QR_CODE)
                .specify(verifierWeIdList)
                .serialize(
                    weIdAuthentication, 
                    list, 
                    new ProtocolProperty(EncodeType.CIPHER, TransMode.DOWNLOAD_MODE)
                );
        System.out.println("将凭证序列化成条码编码:");
        System.out.println(serialize);
        
        // 调用反序列化接口，根据条码编码获取对应数据，自己机构间走本地模式
        // 如果想获取的是PresentationE 将CredentialPojoList修改成PresentationE
        ResponseData<CredentialPojo> deserialize = 
            TransportationFactory.build(TransportationType.QR_CODE)
                .deserialize(weIdAuthentication, serialize.getResult(), CredentialPojo.class);
        System.out.println("根据条码编号获取凭证:");
        System.out.println(deserialize);
        
        // 验证获取到凭证
        ResponseData<Boolean> verify = 
            credentialPojoService.verify(result.getWeId(), deserialize.getResult());
        System.out.println("凭证验证结果:");
        System.out.println(verify);
    }

    /**
     * 构建创建凭证参数.
     * @param weIdAuthentication weId身份信息
     * @return 返回创建凭证参数
     */
    public static CreateCredentialPojoArgs<Map<String, Object>> buildCreateCredentialPojoArgs(
        WeIdAuthentication weIdAuthentication
    ) {

        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs =
            new CreateCredentialPojoArgs<Map<String, Object>>();

        createCredentialPojoArgs.setIssuer(weIdAuthentication.getWeId());
        createCredentialPojoArgs.setExpirationDate(
            System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        createCredentialPojoArgs.setWeIdAuthentication(weIdAuthentication);
        Map<String, Object> claimMap = new HashMap<String, Object>();
        claimMap.put("name", "zhang san");
        claimMap.put("gender", "F");
        claimMap.put("age", 23);
        claimMap.put("id", weIdAuthentication.getWeId());
        createCredentialPojoArgs.setClaim(claimMap);
        return createCredentialPojoArgs;
    }
    
    /**
     * build default CptMapArgs.
     *
     * @param weIdAuthentication weid身份信息
     * @return CptMapArgs
     */
    public static CptMapArgs buildCptArgs(WeIdAuthentication weIdAuthentication) {

        CptMapArgs cptMapArgs = new CptMapArgs();
        cptMapArgs.setCptJsonSchema(buildCptJsonSchema());
        cptMapArgs.setWeIdAuthentication(weIdAuthentication);

        return cptMapArgs;
    }
    
    /**
     * build cpt json schema.
     *
     * @return HashMap
     */
    public static HashMap<String, Object> buildCptJsonSchema() {

        HashMap<String, Object> cptJsonSchemaNew = new HashMap<String, Object>(3);
        cptJsonSchemaNew.put(JsonSchemaConstant.TITLE_KEY, "Digital Identity");
        cptJsonSchemaNew.put(JsonSchemaConstant.DESCRIPTION_KEY, "this is a cpt template");

        HashMap<String, Object> propertitesMap1 = new HashMap<String, Object>(2);
        propertitesMap1.put(JsonSchemaConstant.TYPE_KEY, JsonSchemaConstant.DATA_TYPE_STRING);
        propertitesMap1.put(JsonSchemaConstant.DESCRIPTION_KEY, "this is name");

        String[] genderEnum = {"F", "M"};
        HashMap<String, Object> propertitesMap2 = new HashMap<String, Object>(2);
        propertitesMap2.put(JsonSchemaConstant.TYPE_KEY, JsonSchemaConstant.DATA_TYPE_STRING);
        propertitesMap2.put(JsonSchemaConstant.DATA_TYPE_ENUM, genderEnum);

        HashMap<String, Object> propertitesMap3 = new HashMap<String, Object>(2);
        propertitesMap3.put(JsonSchemaConstant.TYPE_KEY, JsonSchemaConstant.DATA_TYPE_NUMBER);
        propertitesMap3.put(JsonSchemaConstant.DESCRIPTION_KEY, "this is age");

        HashMap<String, Object> propertitesMap4 = new HashMap<String, Object>(2);
        propertitesMap4.put(JsonSchemaConstant.TYPE_KEY, JsonSchemaConstant.DATA_TYPE_STRING);
        propertitesMap4.put(JsonSchemaConstant.DESCRIPTION_KEY, "this is weid");

        HashMap<String, Object> cptJsonSchema = new HashMap<String, Object>(3);
        cptJsonSchema.put("name", propertitesMap1);
        cptJsonSchema.put("gender", propertitesMap2);
        cptJsonSchema.put("age", propertitesMap3);
        cptJsonSchema.put("id", propertitesMap4);
        cptJsonSchemaNew.put(JsonSchemaConstant.PROPERTIES_KEY, cptJsonSchema);

        String[] genderRequired = {"id", "name", "gender"};
        cptJsonSchemaNew.put(JsonSchemaConstant.REQUIRED_KEY, genderRequired);

        return cptJsonSchemaNew;
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
