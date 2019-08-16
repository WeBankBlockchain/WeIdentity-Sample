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

import java.text.ParseException;
import java.util.HashMap;

import com.webank.weid.constant.JsonSchemaConstant;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.response.CreateWeIdDataResult;

/**
 * WeIdentity DID demo.
 *
 * @author v_wbgyang
 */
public class DemoTest extends DemoBase {

    /**
     * set validity period to 360 days by default.
     */
    private static final long EXPIRATION_DATE  = 1000L * 60 * 60 * 24 * 360;

    /**
     * main of demo.
     * @throws ParseException the parseException
     * @throws RuntimeException the runtimeException
     */
    public static void main(String[] args) throws RuntimeException, ParseException {

        // create weId
        CreateWeIdDataResult createWeId = demoService.createWeId();
        BaseBean.print(createWeId);

        // set WeIdentity DID
        demoService.setPublicKey(createWeId);
        demoService.setService(
            createWeId,
            "drivingCardService",
            "https://weidentity.webank.com/endpoint/8377464"
        );
        demoService.setAuthentication(createWeId);

        // get WeId DOM.
        WeIdDocument weIdDom = demoService.getWeIdDom(createWeId.getWeId());
        BaseBean.print(weIdDom);

        // registered authority issuer.
        demoService.registerAuthorityIssuer(createWeId, "webank", "0");

        // registered CPT.
        CptBaseInfo cptResult =
            demoService.registCpt(
                createWeId,
                DemoTest.buildCptJsonSchema()
            );
        BaseBean.print(cptResult);

        long expirationDate = System.currentTimeMillis() + EXPIRATION_DATE;

        // create Credential.
        Credential credential = 
            demoService.createCredential(
                createWeId,
                cptResult.getCptId(),
                DemoTest.buildCptJsonSchemaData(),
                expirationDate
            );
        BaseBean.print(credential);

        // verify the credential.
        boolean result = demoService.verifyCredential(credential);
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
}
