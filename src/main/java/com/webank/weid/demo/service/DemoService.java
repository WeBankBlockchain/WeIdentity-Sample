
package com.webank.weid.demo.service;

import java.util.Map;

import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * demo interface.
 * 
 * @author v_wbgyang
 *
 */
public interface DemoService {

    /**
     * create weId with public and private keys and set related properties.
     * 
     * @param publicKey public key
     * @param privateKey private key
     * @return returns the create weId
     */
    ResponseData<String> createWeIdAndSetAttr(String publicKey, String privateKey);

    /**
     * create weId and set related properties.
     * 
     * @return returns the create weId  and public private keys
     */
    ResponseData<CreateWeIdDataResult> createWeId();

    /**
     * register on the chain as an authoritative body.
     * 
     * @param authorityName the name of the issue
     * @return true is success, false is failure
     */
    ResponseData<Boolean> registerAuthorityIssuer(String issuer, String authorityName);
    
    /**
     * recognize the issuer on chain.
     * 
     * @param issuer the issue
     * @return true is success, false is failure
     */
    ResponseData<Boolean> recognizeAuthorityIssuer(String issuer);

    /**
     * registered CPT.
     * 
     * @param publisher the weId of the publisher
     * @param privateKey the private key of the publisher
     * @param claim claim is CPT
     * @return returns cptBaseInfo
     */
    ResponseData<CptBaseInfo> registCpt(
        String publisher,
        String privateKey,
        Map<String, Object> claim
    );

    /**
     * create credential.
     *
     * @param cptId the cptId of CPT
     * @param issuer the weId of issue
     * @param privateKey the private key of issuer
     * @param claimDate the data of claim
     * @return returns credential
     */
    ResponseData<CredentialPojo> createCredential(
        Integer cptId,
        String issuer,
        String privateKey,
        Map<String, Object> claimDate
    );

    /**
     * verifyEvidence credential.
     * 
     * @param credentialJson credentials in JSON format
     * @return returns the result of verifyEvidence
     */
    ResponseData<Boolean> verifyCredential(String credentialJson);
}
