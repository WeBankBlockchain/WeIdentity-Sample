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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.cpt.Cpt2000000;
import com.webank.weid.cpt.Cpt2000000.Gender;
import com.webank.weid.cpt.Cpt2000001;
import com.webank.weid.cpt.School;
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
    
    /**
     *  build the parameter for create credential.
     * @param cptId the cptId
     * @param weIdData weId information for issue
     * @return the CreateCredentialPojoArgs
     */
    public static CreateCredentialPojoArgs<Cpt2000001> buildCreateArgs2000001(
        Integer cptId,
        CreateWeIdDataResult weIdData) {
        
        CreateCredentialPojoArgs<Cpt2000001> arg = new CreateCredentialPojoArgs<Cpt2000001>();
        arg.setCptId(cptId);
        arg.setIssuer(weIdData.getWeId());
        arg.setExpirationDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 100);
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeIdPrivateKey(weIdData.getUserWeIdPrivateKey());
        arg.setWeIdAuthentication(weIdAuthentication);

        Cpt2000001 claim = new Cpt2000001();
        claim.setName("zhangsan");
        claim.setAge(22.0);
        claim.setGender(Cpt2000001.Gender.M);
        School school = new School();
        school.setName("清华大学");
        school.setAddress("北京");
        claim.setSchool(school);
        arg.setClaim(claim);
        return arg;
    }
    
    /**
     *  build the parameter for create credential.
     * @param cptId the cptId
     * @param weIdData weId information for issue
     * @return the CreateCredentialPojoArgs
     */
    public static CreateCredentialPojoArgs<Cpt2000000> buildCreateArgs2000000(
        Integer cptId,
        CreateWeIdDataResult weIdData) {
        
        CreateCredentialPojoArgs<Cpt2000000> arg = new CreateCredentialPojoArgs<Cpt2000000>();
        arg.setCptId(cptId);
        arg.setIssuer(weIdData.getWeId());
        arg.setExpirationDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 100);
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeIdPrivateKey(weIdData.getUserWeIdPrivateKey());
        arg.setWeIdAuthentication(weIdAuthentication);

        Cpt2000000 claim = new Cpt2000000();
        claim.setName("zhangsan");
        claim.setGender(Gender.F);
        claim.setAge(22.0);
        arg.setClaim(claim);
        return arg;
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
