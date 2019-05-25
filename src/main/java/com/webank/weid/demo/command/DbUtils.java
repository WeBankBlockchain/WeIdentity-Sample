package com.webank.weid.demo.command;

import com.webank.weid.demo.command.BaseBean;
import com.webank.weid.demo.command.DemoBase;
import com.webank.weid.protocol.base.Challenge;
import com.webank.weid.util.DataToolUtils;

public class DbUtils extends DemoBase {

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
    
}
