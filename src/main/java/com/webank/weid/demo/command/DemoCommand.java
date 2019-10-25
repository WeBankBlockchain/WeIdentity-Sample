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
import com.webank.weid.demo.exception.BusinessException;
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
        try {
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
        } catch (Exception e) { 
            logger.error("execute {} failed.",command);
            System.exit(1);
        }
        if (!command.equals("daemon")) {
            System.exit(0);
        }
    }

    private static void daemon() {
        new PolicyServiceImpl();
        BaseBean.print("------------------------------");
        BaseBean.print("the amop server start success");
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
        BaseBean.print("begin to createWeId...");

        // registered weId, authority need to keep their own weId and private keys.
        CreateWeIdDataResult createWeId = demoService.createWeId();

        BaseBean.print("------------------------------");
        BaseBean.print("begin to setPublicKey...");

        // call set public key.
        demoService.setPublicKey(createWeId);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to setAuthenticate...");

        // call set authentication.
        demoService.setAuthentication(createWeId);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to registerAuthorityIssuer...");

        // registered authority, "webank" is the authority Name, "0" is default.
        demoService.registerAuthorityIssuer(createWeId, "webank" + System.currentTimeMillis(), "0");

        BaseBean.print("------------------------------");
        BaseBean.print("begin to regist the first Cpt...");
        
        // registered the first CPT, authority need to keep their own cptId.
        demoService.registCpt(createWeId, SCHEMA1);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to regist the second Cpt...");  
        
        // registered the second CPT, authority need to keep their own cptId.
        demoService.registCpt(createWeId, SCHEMA2);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to regist the third Cpt...");  
        
        // registered the second CPT, authority need to keep their own cptId.
        demoService.registCpt(createWeId, SCHEMA3);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to create the first credential with Map...");
        
        List<CredentialPojo> credentialList = new ArrayList<CredentialPojo>();
        // create the first credential
        CredentialPojo credential1 = 
            demoService.createCredential(DemoUtil.buildCredentialPojo1000(createWeId));
        credentialList.add(credential1);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create the second credential with pojo...");
        
        // create the second credential
        CredentialPojo credential2 = 
            demoService.createCredential(DemoUtil.buildCredentialPojo1001(createWeId));
        credentialList.add(credential2);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create the third credential with JSONString...");
        
        // create the second credential
        CredentialPojo credential3 = 
            demoService.createCredential(DemoUtil.buildCredentialPojo1002(createWeId));
        credentialList.add(credential3);
        
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
        BaseBean.print("------------------------------");
        BaseBean.print("begin to create weId for useragent...");

        Map<String, String> map = new HashMap<String, String>();
        // user registration weId.
        final CreateWeIdDataResult createWeId = demoService.createWeId();
        map.put("userWeId", createWeId.getWeId());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to setAuthenticate...");

        // call set authentication.
        demoService.setAuthentication(createWeId);

        BaseBean.print("------------------------------");
        BaseBean.print("begin to get the credentialList from json...");
        
        // get the CredentialList
        final List<CredentialPojo> credentialList = DemoUtil.getCredentialListFromJson();
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to get the PolicyAndChallenge...");
        
        // mock AMOP request data from other org (1002:verifier orgId, 123456:policyId)
        PolicyAndChallenge policyAndChallenge = 
            DemoUtil.queryPolicyAndChallenge("organizationA", 123456, createWeId.getWeId());
        
        System.out.println(DataToolUtils.serialize(policyAndChallenge));
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to createPresentation...");
        
        // create presentationE
        final PresentationE presentationE = 
            demoService.createPresentation(
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
        String verfierWeId = createWeId.getWeId(); //this is the verfier weId
        verfierWeIds.add(verfierWeId);
        
        String presentationJson = demoService.presentationEToJson(verfierWeIds, presentationE);
        map.put("presentationJson", presentationJson);
        
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to transfom presentation to QRCode ...");
        String presentationQrCode = demoService.presentationEToQrCode(verfierWeIds, presentationE);
        map.put("presentationQrCode", presentationQrCode);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to save the presentation json...");
        // save the presentation JSON
        DemoUtil.saveTemData(map);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin to generateQrCode ...");
        String url = "https://github.com/WeBankFinTech/WeIdentity";
        String imageName = System.currentTimeMillis() + ".jpg";
        String fileName =  DemoUtil.TEMP_DIR + imageName;
        Integer integer = 
            DataToolUtils.generateQrCode(
                url,
                ErrorCorrectionLevel.H, 
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
        BaseBean.print("begin create weid for verifier...");
        
        final CreateWeIdDataResult verifierWeId = demoService.createWeId();
        
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

        // deserialize presentationJson.
        final PresentationE presentationE = 
            demoService.deserializePresentationJson(presentationJson, verifierWeId);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin get the PolicyAndChallenge...");
        
        // get the data from cache.
        final PresentationPolicyE presentationPolicyE = DbUtils.getPolicy("123456");
        Challenge challenge = DbUtils.queryChallenge(presentationE.getNonce());
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin verify presentationE for JSON...");
        String  userWeId = paramMap.get("userWeId");
        // verifyvPresentationE
        boolean result = 
            demoService.verifyPresentationE(
                userWeId,
                presentationPolicyE,
                challenge,
                presentationE
            );
        if (result) {
            BaseBean.print("verify success");
        } else {
            BaseBean.print("verify fail");
            throw new BusinessException("verify fail");
        }
        
        BaseBean.print("------------------------------");
        String presentationQrCode = paramMap.get("presentationQrCode");
        BaseBean.print("getCredentialFromJson presentationQRCode result:");
        BaseBean.print(presentationQrCode);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin deserialize presentationQrCode...");

        // deserialize presentationQrCode.
        final PresentationE presentation = 
            demoService.deserializePresentationQrCode(presentationQrCode, verifierWeId);
        
        BaseBean.print("------------------------------");
        BaseBean.print("begin verify presentationE for QRCode...");
        // verifyvPresentationE
        result = 
            demoService.verifyPresentationE(
                userWeId,
                presentationPolicyE,
                challenge,
                presentation
            );
        if (result) {
            BaseBean.print("verify success");
        } else {
            BaseBean.print("verify fail");
            throw new BusinessException("verify fail");
        }
        
        BaseBean.print("------------------------------");
        BaseBean.print("verifier() finish...");
    }
}
