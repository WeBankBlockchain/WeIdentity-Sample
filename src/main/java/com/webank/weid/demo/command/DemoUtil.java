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

package com.webank.weid.demo.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.cpt.Cpt1000;
import com.webank.weid.cpt.Cpt1002;
import com.webank.weid.cpt.Data;
import com.webank.weid.cpt.Meta;
import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.exception.BusinessException;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.PolicyAndChallenge;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.AmopService;
import com.webank.weid.service.impl.AmopServiceImpl;
import com.webank.weid.util.DataToolUtils;

/**
 * the DemoUtil for command.
 *
 */
public class DemoUtil {

    private static final Logger logger = LoggerFactory.getLogger(DemoUtil.class);

    /**
     * temporary file directory.
     */
    public static final String TEMP_DIR = "./tmp/";

    /**
     * credentials data file.
     */
    private static final String CRED_FILE = TEMP_DIR + "credentials.json";

    /**
     * temporary data file.
     */
    private static final String TEMP_FILE = TEMP_DIR + "temp.data";

    /**
     *  SDK private key.
     */
    public static final String SDK_PRIVATE_KEY;

    static {

        // check the temporary file directory, create it when it does not exists.
        FileUtil.checkDir(TEMP_DIR);

        // getting SDK private key information from a file.
        SDK_PRIVATE_KEY = FileUtil.getDataByPath(PrivateKeyUtil.SDK_PRIVKEY_PATH);
    }

    /**
     * read credential list.
     * @return object of Credential
     */
    public static List<CredentialPojo> getCredentialListFromJson() {

        // get credential string from the file.
        String jsonStr = FileUtil.getDataByPath(CRED_FILE);
        
        // converting credential string to Credential List.
        List<CredentialPojo> credentialList = null;
        try {
            credentialList = DataToolUtils.deserializeToList(jsonStr, CredentialPojo.class);
        }  catch (Exception e) {
            logger.error("deserialize credentialListJson failed,e:{}",e);
        }
        return credentialList;
    }

    /**
     * get temporary data from file.
     * 
     * @return return map format data
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getTempDataFromFile() {

        // getting authority data from temporary file.
        String json = FileUtil.getDataByPath(DemoUtil.TEMP_FILE);
        ObjectMapper om = new ObjectMapper();
        Map<String, String> paramMap = null;
        try {
            paramMap = om.readValue(json, Map.class);
        } catch (IOException e) {
            logger.error("read temp.data error", e);
        }
        return paramMap;
    }

    /**
     * save credential in a specified file.
     * 
     * @param credentialList require
     * @return return to save path
     */
    public static String saveCredentialList(List<CredentialPojo> credentialList) {

        // converting Credential object to JSON string.
        String dataStr = DemoUtil.formatObjectToString(credentialList);

        // save the JSON string in the file.
        return FileUtil.saveFile(CRED_FILE, dataStr);
        
    }

    /**
     * save temporary data.
     * 
     * @param map require
     */
    public static void saveTemData(Map<String, String> map) {

        // converting Map to JSON string.
        String dataStr = DemoUtil.formatObjectToString(map);

        // save the JSON string in the file.
        FileUtil.saveFile(TEMP_FILE, dataStr);
    }

    /**
     * format Object to String.
     * 
     * @return return JSON string
     */
    public static String formatObjectToString(Object obj) {
        return DataToolUtils.serialize(obj);
    }
    
    /**
     * save data into specific file.
     * 
     * @param fileName the fileName
     * @param obj the data to save
     */
    public static void saveDataInFile(String fileName, Object obj) {
        FileUtil.saveFile(TEMP_DIR + fileName + ".data", formatObjectToString(obj));
    }
    
    /**
     * query data from file.
     * 
     * @param fileName the fileName
     * @return return String data
     */
    public static String queryDataFromFile(String fileName) {
        return FileUtil.getDataByPath(TEMP_DIR + fileName + ".data");
    }

    private static WeIdAuthentication buildWeIdAuthentication(CreateWeIdDataResult weIdData) {
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(weIdData.getWeId());
        weIdAuthentication.setWeIdPublicKeyId(weIdData.getWeId() + "#keys-0");
        weIdAuthentication.setWeIdPrivateKey(weIdData.getUserWeIdPrivateKey());
        return weIdAuthentication;
    }
    
    /**
     *  build the parameter for create credential.
     * @param createResult weId information for issue
     * @return the CreateCredentialPojoArgs
     */
    public static CreateCredentialPojoArgs<Map<String, Object>> buildCredentialPojo1000(
        CreateWeIdDataResult createResult
    ) {

       
        Map<String, Object> cpt1000 = new HashMap<String, Object>();
        cpt1000.put("weid", createResult.getWeId());
        cpt1000.put("age", 23);
        cpt1000.put("gender", Cpt1000.Gender.F);
        cpt1000.put("name", "张三");
        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs =
            new CreateCredentialPojoArgs<Map<String, Object>>();
        createCredentialPojoArgs.setClaim(cpt1000);
        createCredentialPojoArgs.setCptId(1000);
        createCredentialPojoArgs.setExpirationDate(System.currentTimeMillis() + (3600 * 24 * 1000));
        createCredentialPojoArgs.setIssuer(createResult.getWeId());
        createCredentialPojoArgs.setWeIdAuthentication(buildWeIdAuthentication(createResult));
        return createCredentialPojoArgs;
    }

    /**
     *  build the parameter for create credential.
     * @param createResult weId information for issue
     * @return the CreateCredentialPojoArgs
     */
    public static CreateCredentialPojoArgs<Cpt1002> buildCredentialPojo1002(
        CreateWeIdDataResult createResult
    ) {
        
        Data data = new Data();
        data.setId("123");
        data.setSipTellAddress("sipTellAddress");
        data.setUserlevel(1);
        Cpt1002 cpt1002 = new Cpt1002();
        cpt1002.setData(data);
        
        cpt1002.setName("test1001");
        
        Meta meta = new Meta();
        meta.setCode(12.2);
        meta.setError("metaError");
        meta.setInfo("metaInfo");
        
        cpt1002.setMeta(meta);
        cpt1002.setWeid(createResult.getWeId());
        
        CreateCredentialPojoArgs<Cpt1002> createCredentialPojoArgs =
            new CreateCredentialPojoArgs<Cpt1002>();
        createCredentialPojoArgs.setClaim(cpt1002);
        createCredentialPojoArgs.setCptId(1002);
        createCredentialPojoArgs.setExpirationDate(System.currentTimeMillis() + (3600 * 24 * 1000));
        createCredentialPojoArgs.setIssuer(createResult.getWeId());
        createCredentialPojoArgs.setWeIdAuthentication(buildWeIdAuthentication(createResult));
        return createCredentialPojoArgs;
    }

    /**
     *  build the parameter for create credential.
     * @param createResult weId information for issue
     * @return the CreateCredentialPojoArgs
     */
    public static CreateCredentialPojoArgs<String> buildCredentialPojo1001(
        CreateWeIdDataResult createResult
    ) {
        // 创建原始凭证
        CreateCredentialPojoArgs<String> createCredentialPojoArgs =
                new CreateCredentialPojoArgs<String>();
        createCredentialPojoArgs.setClaim(DemoBase.CLAIMDATA);
        createCredentialPojoArgs.setCptId(1001);
        createCredentialPojoArgs.setExpirationDate(System.currentTimeMillis() + (3600 * 24 * 1000));
        createCredentialPojoArgs.setIssuer(createResult.getWeId());
        createCredentialPojoArgs.setWeIdAuthentication(buildWeIdAuthentication(createResult));
        return createCredentialPojoArgs;
    }
    
    /**
     * send AMOP message for get the policyAndChallenge.
     * @param orgId send to orgId
     * @param policyId the policyId
     * @return the policyAndChallenge
     */
    public static PolicyAndChallenge queryPolicyAndChallenge(
        String orgId,
        Integer policyId,
        String targetWeId
    ) {

        AmopService amopService = new AmopServiceImpl();
        ResponseData<PolicyAndChallenge> response = 
            amopService.getPolicyAndChallenge(orgId, policyId, targetWeId);
        
        BaseBean.print("queryPolicyAndChallenge from amop result:");
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error(
                "queryPolicyAndChallenge from amop failed,responseData:{}",
                response
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }
}
