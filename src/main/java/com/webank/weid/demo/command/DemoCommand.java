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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.bcos.contract.tools.ToolConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.response.CreateWeIdDataResult;

/**
 * command operation.
 * 
 * @author v_wbpenghu
 */
public class DemoCommand {

    // SDK private key.
    static final String PRIVATEKEY;
    
    private static final  ApplicationContext context;
    
    private static final Logger logger = LoggerFactory.getLogger(DemoCommand.class);

    /**
     * schema.
     */
    private static final String SCHEMA;
    
    /**
     * claimData.
     */
    private static final String CLAIMDATA;
    
    /**
     * Hexadecimal.
     */
    private static final int HEXADECIMAL = 16;

    static {
        context = new ClassPathXmlApplicationContext(new String[] {
            "classpath:applicationContext.xml",
            "classpath:SpringApplicationContext-demo.xml"});

        ToolConf toolConf = context.getBean(ToolConf.class);
        PRIVATEKEY = new BigInteger(toolConf.getPrivKey(), HEXADECIMAL).toString();
        
        //get jsonSchema.
        SCHEMA = FileUtil.getDataByPath("./claim/JsonSchema.json");
        //get schemaData.
        CLAIMDATA = FileUtil.getDataByPath("./claim/ClaimData.json");
    }


    /**
     * main.
     */
    public static void main(String[] args) {

        if (null == args || args.length != 1) {
            args = new String[1];
        }
        
        String command = args[0];
        
        if (command == null) {
            command = "default";
        }
        
        switch (command) {
            case "issuer":
                issue();
                break;
            case "user":
                user();
                break;
            case "verifier":
                verify();
                break;
            default:
                issue();
                break;
        }
        System.exit(0);
    }

    /**
     * interface invocation process.
     * 1, create weId
     * 2, set attribute
     * 3, registered authority
     * 4, publish CPT
     */
    private static void issue() {
        
        BaseBean.print("issue() init...");
        DemoService demo = context.getBean(DemoService.class);
        Map<String, String> map = new HashMap<String, String>();
        
        BaseBean.print("begin createWeId...");
        // registered weId, authority need to keep their own weId and private keys.
        CreateWeIdDataResult createWeId = demo.createWeId();
        map.put("weId", createWeId.getWeId());
        map.put("privateKey", createWeId.getUserWeIdPrivateKey().getPrivateKey());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin setPublicKey...");
        // call set public key, the type default "secp256k1".
        demo.setPublicKey(createWeId, "secp256k1");
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin setAuthenticate...");
        // call set authentication, the type default "RsaSignatureAuthentication2018".
        demo.setAuthenticate(createWeId, "RsaSignatureAuthentication2018");
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin registerAuthorityIssuer...");
        // registered authority, "webank" is the authority Name, "0" is default.
        demo.registerAuthorityIssuer(createWeId, "webank", "0");

        BaseBean.print("------------------------------");
        BaseBean.print("begin registCpt...");
        // registered CPT, authority need to keep their own cptId.
        CptBaseInfo cptResult = demo.registCpt(createWeId, SCHEMA);
        map.put("cptId", cptResult.getCptId().toString());
        
        DemoUtil.saveTemData(map);
        
        BaseBean.print("------------------------------");
        BaseBean.print("issue() finish...");
    }
    
    /**
     * interface invocation process.
     * 1, create weId
     * 2, provide CPT template data, authority issue credential for them
     */
    private static void user() {
        
        BaseBean.print("user() init...");
        DemoService demo = context.getBean(DemoService.class);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin createWeId...");
        // user registration weId.
        final CreateWeIdDataResult createWeId = demo.createWeId();

        Map<String, String> paramMap = DemoUtil.getTempDataFromFile();
        if (null == paramMap) {
            logger.error("read temp.data is null");
            return;
        }
        
        // authority create credentials based on data provided by users.
        BaseBean.print("------------------------------");
        BaseBean.print("begin createCredential...");
        CreateWeIdDataResult weIdResult = new CreateWeIdDataResult();
        weIdResult.setWeId(paramMap.get("weId"));
        weIdResult.setUserWeIdPrivateKey(new WeIdPrivateKey());
        weIdResult.getUserWeIdPrivateKey().setPrivateKey(paramMap.get("privateKey"));

        long expirationDate = System.currentTimeMillis() + (1000L * 60 * 24);
        // Number of CPT issued by authority.
        Integer cptId = Integer.valueOf(paramMap.get("cptId"));

        String claimDataTmp = CLAIMDATA;
        // user weId is used as part of CPT data to identify credential attribution.
        claimDataTmp =  claimDataTmp.replace("{userWeId}", createWeId.getWeId());
        
        Credential credential =
            demo.createCredential(
                weIdResult, 
                cptId, 
                claimDataTmp, 
                expirationDate);

        // save the credentials in a file as JSON strings.
        BaseBean.print("------------------------------");
        BaseBean.print("beign saveCredential...");
        String path = DemoUtil.saveCredential(credential);
        BaseBean.print("saveCredential success, path:" + path);
        
        BaseBean.print("------------------------------");
        BaseBean.print("user() finish...");
    }

    /**
     * interface invocation process.
     * 1, verify user-provided credentials
     */
    private static void verify() {
        
        BaseBean.print("verify() init...");
        DemoService demo = context.getBean(DemoService.class);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin getCredentialFromJson...");
        // get user credentials from file.
        Credential credential = DemoUtil.getCredentialFromJson();
        BaseBean.print("getCredentialFromJson result:");
        BaseBean.print(credential);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin verifyCredential...");
        boolean result = demo.verifyCredential(credential);
        if (result) {
            BaseBean.print("verify success");
        } else {
            BaseBean.print("verify fail");
        }
        
        BaseBean.print("------------------------------");
        BaseBean.print("verify() finish...");
    }
}
