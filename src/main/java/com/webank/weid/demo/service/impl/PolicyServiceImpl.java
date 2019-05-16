package com.webank.weid.demo.service.impl;

import com.webank.weid.demo.common.util.DbUtils;
import com.webank.weid.protocol.base.Challenge;
import com.webank.weid.protocol.base.PolicyAndChallenge;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.service.impl.callback.PresentationPolicyService;

public class PolicyServiceImpl extends PresentationPolicyService {
    
    @Override
    public PolicyAndChallenge policyAndChallengeOnPush(String policyId) {
       
        //获取presentationPolicyE
        PresentationPolicyE presentationPolicyE = 
            DbUtils.queryPresentationPolicyE(Integer.valueOf(policyId));
        //获取Challenge
        Challenge challenge = DbUtils.queryChallenge("abcd1234");
        
        PolicyAndChallenge policyAndChallenge = new PolicyAndChallenge();
        policyAndChallenge.setChallenge(challenge);
        policyAndChallenge.setPresentationPolicyE(presentationPolicyE);
        return policyAndChallenge;
    }
}