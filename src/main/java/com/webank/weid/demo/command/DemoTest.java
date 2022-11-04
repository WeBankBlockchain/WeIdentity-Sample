
package com.webank.weid.demo.command;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.webank.weid.constant.CredentialType;
import com.webank.weid.constant.JsonSchemaConstant;
import com.webank.weid.demo.common.dto.PasswordKey;
import com.webank.weid.protocol.base.*;
import com.webank.weid.protocol.request.AuthenticationArgs;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.request.CreateWeIdArgs;
import com.webank.weid.protocol.request.ServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.util.DataToolUtils;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.utils.Numeric;

/**
 * WeIdentity DID demo.
 *
 * @author v_wbgyang
 */
public class DemoTest extends DemoBase {

    /**
     * main of demo.
     * @throws ParseException the parseException
     * @throws RuntimeException the runtimeException
     */
    public static void main(String[] args) throws RuntimeException, ParseException {

        // create weId
        CreateWeIdDataResult createWeId = demoService.createWeId();
        BaseBean.print(createWeId);

        CreateWeIdArgs createWeIdArgs = buildCreateWeIdArgs();
        WeIdPublicKey weIdPublicKey = new WeIdPublicKey();
        weIdPublicKey.setPublicKey(createWeIdArgs.getPublicKey());
        String createWeId1 = demoService.createWeIdByPublicKey(weIdPublicKey, createWeIdArgs.getWeIdPrivateKey());
        BaseBean.print(createWeId1);

        String createWeId2 = demoService.createWeId(createWeIdArgs);
        BaseBean.print(createWeId2);

        String getWeIdDocumentJson = demoService.getWeIdDocumentJson(createWeId.getWeId());
        BaseBean.print(getWeIdDocumentJson);

        ServiceArgs setServiceArgs = buildSetServiceArgs(createWeId);

        demoService.setService(createWeId.getWeId(),
                setServiceArgs, createWeId.getUserWeIdPrivateKey());

//        demoService.setAuthentication(createWeId);

        WeIdDocumentMetadata getWeIdDocumentMetadata = demoService.getWeIdDocumentMetadata(createWeId.getWeId());
        BaseBean.print(getWeIdDocumentMetadata);

        boolean isWeIdExist = demoService.isWeIdExist(createWeId.getWeId());
        BaseBean.print(isWeIdExist);

        boolean isDeactivated = demoService.isWeIdExist(createWeId.getWeId());
        BaseBean.print(isDeactivated);

        AuthenticationArgs setAuthenticationArgs = new AuthenticationArgs();
        setAuthenticationArgs.setController(createWeId.getWeId());
        setAuthenticationArgs
                .setPublicKey(createWeId.getUserWeIdPublicKey().getPublicKey());
        boolean revokeAuthentication = demoService.revokeAuthentication(
                createWeId.getWeId(),
                setAuthenticationArgs,
                createWeId.getUserWeIdPrivateKey());
        BaseBean.print(revokeAuthentication);

        // get WeId DOM.
        WeIdDocument weIdDom = demoService.getWeIdDom(createWeId.getWeId());
        BaseBean.print(weIdDom);

        // registered authority issuer.
//        demoService.registerAuthorityIssuer(
//            createWeId, String.valueOf(System.currentTimeMillis()), "0");
        // recognize AuthorityIssuer
//        demoService.recognizeAuthorityIssuer(createWeId);
        
        // registered CPT.
        CptBaseInfo cptResult =
            demoService.registCpt(
                createWeId,
                DemoTest.buildCptJsonSchema()
            );
        BaseBean.print(cptResult);

        // create Credential.
        String publicKeyId = createWeId.getUserWeIdPublicKey().getPublicKey();
        WeIdAuthentication weIdAuthentication = buildWeIdAuthority(createWeId, publicKeyId);
        CreateCredentialPojoArgs<Map<String, Object>>  createCredentialPojoArgs = 
            buildCreateCredentialPojoArgs(cptResult.getCptId(), weIdAuthentication);
        CredentialPojo credential = 
            demoService.createCredential(createCredentialPojoArgs);
        BaseBean.print(credential);

        // verify the credential.
        boolean result = demoService.verifyCredentialPojo(credential);
        if (result) {
            BaseBean.print("verify success");
        } else {
            BaseBean.print("verify fail");
        }
    }

    /**
     * build cpt json schema.
     * @return HashMap
     */
    public static HashMap<String, Object> buildCptJsonSchema() {

        HashMap<String, Object> cptJsonSchemaNew = new HashMap<String, Object>(3);
        cptJsonSchemaNew.put(JsonSchemaConstant.TITLE_KEY, "cpt template");
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
        cptJsonSchema.put("weid", propertitesMap4);

        cptJsonSchemaNew.put(JsonSchemaConstant.PROPERTIES_KEY, cptJsonSchema);

        String[] genderRequired = {"name", "gender"};
        cptJsonSchemaNew.put(JsonSchemaConstant.REQUIRED_KEY, genderRequired);

        return cptJsonSchemaNew;
    }

    /**
     * build cpt json schemaData.
     * @return HashMap
     */
    public static HashMap<String, Object> buildCptJsonSchemaData() {

        HashMap<String, Object> cptJsonSchemaData = new HashMap<String, Object>(3);
        cptJsonSchemaData.put("name", "zhang san");
        cptJsonSchemaData.put("gender", "F");
        cptJsonSchemaData.put("age", 18);
        cptJsonSchemaData.put("weid", "did:weid:0x566a07b553804266133f130c8c0bf6fede406984");
        return cptJsonSchemaData;
    }
    
    /**
     * 构建创建凭证参数.
     * @param weIdAuthentication weId身份信息
     * @return 返回创建凭证参数
     */
    public static CreateCredentialPojoArgs<Map<String, Object>> buildCreateCredentialPojoArgs(
        Integer cptId,
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
        createCredentialPojoArgs.setType(CredentialType.ORIGINAL);
        createCredentialPojoArgs.setCptId(cptId);
        return createCredentialPojoArgs;
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

    /**
     * build default CreateWeIdArgs.
     *
     * @return CreateWeIdArgs
     */
    public static CreateWeIdArgs buildCreateWeIdArgs() {
        CreateWeIdArgs args = new CreateWeIdArgs();
        PasswordKey passwordKey = createEcKeyPair();
        args.setPublicKey(passwordKey.getPublicKey());

        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(passwordKey.getPrivateKey());

        args.setWeIdPrivateKey(weIdPrivateKey);

        return args;
    }

    /**
     * create a new public key - private key.
     *
     * @return PasswordKey
     */
    public static PasswordKey createEcKeyPair() {

        PasswordKey passwordKey = new PasswordKey();
        CryptoKeyPair keyPair = DataToolUtils.cryptoSuite.createKeyPair();
        BigInteger bigPublicKey =
                new BigInteger(1, Numeric.hexStringToByteArray(keyPair.getHexPublicKey()));
        BigInteger bigPrivateKey =
                new BigInteger(1, Numeric.hexStringToByteArray(keyPair.getHexPrivateKey()));

        String publicKey = String.valueOf(bigPublicKey);
        String privateKey = String.valueOf(bigPrivateKey);
        passwordKey.setPrivateKey(privateKey);
        passwordKey.setPublicKey(publicKey);
        return passwordKey;
    }

    /**
     * buildSetPublicKeyArgs.
     *
     * @param createWeId WeId
     * @return SetServiceArgs
     */
    public static ServiceArgs buildSetServiceArgs(CreateWeIdDataResult createWeId) {

        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setType("drivingCardService");
        serviceArgs.setServiceEndpoint("https://weidentity.webank.com/endpoint/xxxxx");
        return serviceArgs;
    }
}
