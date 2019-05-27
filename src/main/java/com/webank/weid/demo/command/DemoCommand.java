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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.service.impl.PolicyServiceImpl;
import com.webank.weid.protocol.base.Challenge;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.PolicyAndChallenge;
import com.webank.weid.protocol.base.PresentationE;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.util.DataToolUtils;

/**
 * command operation.
 * 
 * @author v_wbpenghu
 */
public class DemoCommand extends DemoBase {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoCommand.class);

    /**
     * the main for command mode.
     * 
     * @param args input parameters, expressed as roles, such as issuer, user, verifier
     */
    public static void main(String[] args) {

        logger.info("args = {}", Arrays.toString(args));
        
        // check the length.
        if (null == args || args.length != 1) {
            args = new String[1];
        }

        String command = args[0];
        
        // check the first parameter.
        if (command == null) {
            command = "default";
        }
        
        // do different operations according to different roles.
        switch (command) {
            case "issuer":
                issuer();
                break;
            case "user_agent":
                userAgent();
                break;
            case "verifier":
                verifier();
                break;
            case "daemon":
                daemon();
                break;
            default:
                issuer();
                break;
        }
        if (!command.equals("daemon")) {
            System.exit(0);
        }
    }

    private static void daemon() {
        new PolicyServiceImpl();
    }
    
    /**
     * interface invocation process.
     * 1, create weId
     * 2, set attribute
     * 3, registered authority
     * 4, publish CPT
     */
    private static void issuer() {

        BaseBean.print("issuer() init...");
        DemoService demo = context.getBean(DemoService.class);
        
        BaseBean.print("begin to createWeId...");

        // registered weId, authority need to keep their own weId and private keys.
        CreateWeIdDataResult createWeId = demo.createWeId();

        BaseBean.print("------------------------------");
        BaseBean.print("begin to setPublicKey...");

        // call set public key, the type default "secp256k1".
        demo.setPublicKey(createWeId, "secp256k1");

        BaseBean.print("------------------------------");
        BaseBean.print("begin to setAuthenticate...");

        // call set authentication.
        demo.setAuthentication(createWeId);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to registerAuthorityIssuer...");

        // registered authority, "webank" is the authority Name, "0" is default.
        demo.registerAuthorityIssuer(createWeId, "webank" + System.currentTimeMillis(), "0");

        BaseBean.print("------------------------------");
        BaseBean.print("begin to regist the first Cpt...");
        
        // registered the first CPT, authority need to keep their own cptId.
        demo.registCpt(createWeId, SCHEMA1);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to regist the second Cpt...");  
        
        // registered the second CPT, authority need to keep their own cptId.
        demo.registCpt(createWeId, SCHEMA2);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to create the first credential with pojo...");
        
        // create the first credential
        CredentialPojo credential1 = 
            demo.createCredential(DemoUtil.buildCreateArgs2000000(2000000, createWeId));
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create the second credential with pojo...");
        
        // create the second credential
        CredentialPojo credential2 = 
            demo.createCredential(DemoUtil.buildCreateArgs2000001(2000001, createWeId));

        List<CredentialPojo> credentialList = new ArrayList<CredentialPojo>();
        credentialList.add(credential1);
        credentialList.add(credential2);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create credential with map...");
        //create credential with map
        CredentialPojo credential3 = 
            demo.createCredential(DemoUtil.buildCreateArgsWithMap(2000001, createWeId));
        BaseBean.print("the credential id:" + credential3.getId());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create credential with json...");
        //create credential with json
        CredentialPojo credential4 = 
                demo.createCredential(DemoUtil.buildCreateArgsWithJsonString(2000001, createWeId));
        BaseBean.print("the credential id:" + credential4.getId());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to save the credentialList...");
        DemoUtil.saveCredentialList(credentialList);
        
        BaseBean.print("------------------------------");
        BaseBean.print("issuer() finish...");
    }

    /**
     * interface invocation process.
     * 1, create weId
     * 2, provide CPT template data, authority issue credential for them
     */
    private static void userAgent() {

        BaseBean.print("userAgent() init...");
        DemoService demo = context.getBean(DemoService.class);
       
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create weId for useragent...");

        Map<String, String> map = new HashMap<String, String>();
        // user registration weId.
        final CreateWeIdDataResult createWeId = demo.createWeId();
        map.put("userWeId", createWeId.getWeId());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to setAuthenticate...");

        // call set authentication.
        demo.setAuthentication(createWeId);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to get the credentialList from json...");
        
        // get the CredentialList
        final List<CredentialPojo> credentialList = DemoUtil.getCredentialListFromJson();
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to get the PolicyAndChallenge...");
        
        // mock AMOP request data from other org (1002:verifier orgId, 123456:policyId)
        PolicyAndChallenge policyAndChallenge = 
            DemoUtil.queryPolicyAndChallenge("1002", 1001, createWeId.getWeId());
        
        System.out.println(DataToolUtils.serialize(policyAndChallenge));
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to createPresentation...");
        
        // create presentationE
        final PresentationE presentationE = 
            demo.createPresentation(
                credentialList, 
                policyAndChallenge.getPresentationPolicyE(), 
                policyAndChallenge.getChallenge(), 
                createWeId
            );
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to transfom presentation to json ...");
        
        // serialize presentationE
        // build same verfier for get the key if use CIPHER to serialize
        List<String> verfierWeIds = new ArrayList<>();
        String verfierWeId = ""; //this is the verfier weId
        verfierWeIds.add(verfierWeId);
        
        String presentationJson = demo.presentationEToJson(verfierWeIds, presentationE);
        map.put("presentationJson", presentationJson);
        
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to transfom presentation to QRCode ...");
        String presentationQrCode = demo.presentationEToQrCode(verfierWeId, presentationE);
        map.put("presentationQrCode", presentationQrCode);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to save the presentation json...");
        // save the presentation JSON
        DemoUtil.saveTemData(map);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to generateQrCode ...");
        String imageName = System.currentTimeMillis() + ".jpg";
        String fileName =  DemoUtil.TEMP_DIR + imageName;
        Integer integer = 
            DataToolUtils.generateQrCode(
                presentationQrCode,
                ErrorCorrectionLevel.L, 
                fileName
            );
        if (integer == ErrorCode.SUCCESS.getCode()) {
            BaseBean.print("generateQrCode success in file: " + fileName);
        } else {
            BaseBean.print("generateQrCode faile, please check the log.");
        }
        
        
        BaseBean.print("------------------------------");
        BaseBean.print("userAgent() finish...");
    }

    /**
     * interface invocation process.
     * 1, verify user-provided credentials
     */
    private static void verifier() {

        BaseBean.print("verifier() init...");

        BaseBean.print("------------------------------");
        BaseBean.print("begin get the presentation json...");

        // get user credentials from file.
        Map<String, String> paramMap = DemoUtil.getTempDataFromFile();
        if (null == paramMap) {
            logger.error("read temp.data is null");
            return;
        }
        String presentationJson = paramMap.get("presentationJson");
        BaseBean.print("getCredentialFromJson presentationJson result:");
        BaseBean.print(presentationJson);

        BaseBean.print("------------------------------");
        BaseBean.print("begin deserialize presentationJson...");
        
        DemoService demo = context.getBean(DemoService.class);
        
        // deserialize presentationJson.
        final PresentationE presentationE = demo.deserializePresentationJson(presentationJson);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin get the PolicyAndChallenge...");
        
        // get the data from cache.
        final PresentationPolicyE presentationPolicyE = PolicyServiceImpl.policMap.get("1001");
        Challenge challenge = DbUtils.queryChallenge(presentationE.getNonce());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin verify presentationE for JSON...");
        String  userWeId = paramMap.get("userWeId");
        // verifyvPresentationE
        boolean result = 
            demo.verifyPresentationE(userWeId, presentationPolicyE, challenge, presentationE);
        if (result) {
            BaseBean.print("verify success");
        } else {
            BaseBean.print("verify fail");
        }
        
        BaseBean.print("------------------------------");
        String presentationQrCode = paramMap.get("presentationQrCode");
        BaseBean.print("getCredentialFromJson presentationQRCode result:");
        BaseBean.print(presentationQrCode);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin deserialize presentationQrCode...");

        // deserialize presentationQrCode.
        final PresentationE presentation = demo.deserializePresentationQrCode(presentationQrCode);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin verify presentationE for QRCode...");
        // verifyvPresentationE
        result = demo.verifyPresentationE(userWeId, presentationPolicyE, challenge, presentation);
        if (result) {
            BaseBean.print("verify success");
        } else {
            BaseBean.print("verify fail");
        }
        
        BaseBean.print("------------------------------");
        BaseBean.print("verifier() finish...");
    }
}
