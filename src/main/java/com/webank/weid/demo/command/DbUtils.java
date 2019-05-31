package com.webank.weid.demo.command;

import java.util.HashMap;
import java.util.Map;

import com.webank.weid.demo.command.BaseBean;
import com.webank.weid.protocol.base.Challenge;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.util.DataToolUtils;

public class DbUtils {
    
    private static final Map<String, PresentationPolicyE> policMap = 
        new HashMap<String, PresentationPolicyE>();
    
    static {
        //initialization policy
        //you can do this : PresentationPolicyE.createByJson("");
        PresentationPolicyE presentationPolicyE = PresentationPolicyE.create("policy123456.json");
        policMap.put("123456", presentationPolicyE);
    }
    
    /**
     * mock query the challenge form DB.
     * @param nonce the nonce
     * @return the challenge
     */
    public static Challenge queryChallenge(String nonce) {
        String fileName = DataToolUtils.sha3(nonce);
        String value = DemoUtil.queryDataFromFile(fileName);
        Challenge challenge = DataToolUtils.deserialize(value, Challenge.class);
        BaseBean.print("queryChallenge result:");
        BaseBean.print(challenge);
        return challenge;
    }
    
    /**
     * mock save data into DB.
     * @param key the key
     * @param data the data for save
     */
    public static void save(String key, Object data) {
        String fileName = DataToolUtils.sha3(key);
        DemoUtil.saveDataInFile(fileName, data);
    }
    
    /**
     * mock get the policy from your DB.
     * @param policyId the policyId
     */
    public static PresentationPolicyE getPolicy(String policyId) {
        return policMap.get(policyId);
    } 
}
