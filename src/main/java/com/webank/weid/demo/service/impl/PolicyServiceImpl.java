package com.webank.weid.demo.service.impl;

import com.webank.weid.demo.command.DbUtils;
import com.webank.weid.protocol.base.Challenge;
import com.webank.weid.protocol.base.PolicyAndChallenge;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.service.impl.callback.PresentationPolicyService;

public class PolicyServiceImpl extends PresentationPolicyService {

    @Override
    public PolicyAndChallenge policyAndChallengeOnPush(String policyId, String targetWeId) {
        
        //获取presentationPolicyE
        PresentationPolicyE presentationPolicyE = DbUtils.getPolicy(policyId);
        
        //获取Challenge
        Challenge challenge = 
            Challenge.create(targetWeId, String.valueOf(System.currentTimeMillis()));
        
        //保存challenge到数据库
        DbUtils.save(challenge.getNonce(), challenge);
        
        PolicyAndChallenge policyAndChallenge = new PolicyAndChallenge();
        policyAndChallenge.setChallenge(challenge);
        policyAndChallenge.setPresentationPolicyE(presentationPolicyE);
        return policyAndChallenge;
    }
}