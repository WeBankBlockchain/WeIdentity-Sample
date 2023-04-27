
package com.webank.weid.demo.service.impl;

import com.webank.weid.constant.CredentialType;
import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.*;
import com.webank.weid.protocol.request.*;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.service.impl.AuthorityIssuerServiceImpl;
import com.webank.weid.service.impl.CptServiceImpl;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;
import com.webank.weid.service.rpc.AuthorityIssuerService;
import com.webank.weid.service.rpc.CptService;
import com.webank.weid.service.rpc.CredentialPojoService;
import com.webank.weid.service.rpc.WeIdService;
import com.webank.weid.util.DataToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Demo service.
 *
 * @author v_wbgyang
 */
@Service
public class DemoServiceImpl implements DemoService {

    static {
        FileUtil.loadConfigFromEnv();
    }

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    private AuthorityIssuerService authorityIssuerService = new AuthorityIssuerServiceImpl();

    private CptService cptService = new CptServiceImpl();

    private CredentialPojoService credentialService = new CredentialPojoServiceImpl();

    private WeIdService weIdService = new WeIdServiceImpl();

    /**
     * set validity period to 360 days by default.
     */
    private static final long EXPIRATION_DATE  = 1000L * 60 * 60 * 24 * 365 * 100;


    /**
     * create weId with public and private keys and set related properties.
     * 
     * @param publicKey public key
     * @param privateKey private key
     * @return returns the create weId
     */
    public ResponseData<String> createWeIdAndSetAttr(String publicKey, String privateKey) {

        logger.info("begin create weId and set attribute without parameter");

        // 1, create weId using the incoming public and private keys
        CreateWeIdArgs createWeIdArgs = new CreateWeIdArgs();
        createWeIdArgs.setPublicKey(publicKey);
        createWeIdArgs.setWeIdPrivateKey(new WeIdPrivateKey());
        createWeIdArgs.getWeIdPrivateKey().setPrivateKey(privateKey);
        ResponseData<String> createResult = weIdService.createWeId(createWeIdArgs);
        logger.info("createWeIdAndSetAttr response:{}", createResult);
        if (createResult.getErrorCode().intValue() != ErrorCode.SUCCESS.getCode()) {
            return createResult;
        }

        PrivateKeyUtil.savePrivateKey(
            PrivateKeyUtil.KEY_DIR,
            createResult.getResult(),
            privateKey
        );

        /*CreateWeIdDataResult weIdData = new CreateWeIdDataResult();
        weIdData.setWeId(createResult.getResult());
        weIdData.setUserWeIdPrivateKey(new WeIdPrivateKey());
        weIdData.getUserWeIdPrivateKey().setPrivateKey(privateKey);
        weIdData.setUserWeIdPublicKey(new WeIdPublicKey());
        weIdData.getUserWeIdPublicKey().setPublicKey(publicKey);

        // 2, call set public key
        ResponseData<Boolean> setPublicKeyRes = this.setPublicKey(weIdData);
        if (!setPublicKeyRes.getResult()) {
            createResult.setErrorCode(
                ErrorCode.getTypeByErrorCode(setPublicKeyRes.getErrorCode())
            );
            return createResult;
        }

        // 3, call set authentication
        ResponseData<Boolean> setAuthenticateRes = this.setAuthentication(weIdData);
        if (!setAuthenticateRes.getResult()) {
            createResult.setErrorCode(
                ErrorCode.getTypeByErrorCode(setAuthenticateRes.getErrorCode())
            );
            return createResult;
        }*/
        return createResult;
    }


    /**
     * 创建weid.
     * @return
     */
    public ResponseData<CreateWeIdDataResult> createWeId() {

        ResponseData<CreateWeIdDataResult> response = createWeIdWithSetAttr();
        // if weId is created successfully, save its private key.
        if (response.getErrorCode().intValue() == ErrorCode.SUCCESS.getCode()) {
            PrivateKeyUtil.savePrivateKey(
                PrivateKeyUtil.KEY_DIR,
                response.getResult().getWeId(),
                response.getResult().getUserWeIdPrivateKey().getPrivateKey()
            );
        }

        /*
         *  private keys are not allowed to be transmitted over http, so this place
         *  annotates the return of private keys to avoid misuse.
         */
        response.getResult().setUserWeIdPrivateKey(null);
        return response;
    }

    /**
     * create weId and set related properties.
     * 
     * @return returns the create weId and public private keys
     */
    private ResponseData<CreateWeIdDataResult> createWeIdWithSetAttr() {

        logger.info("begin create weId and set attribute");

        // 1, create weId, this method automatically creates public and private keys
        ResponseData<CreateWeIdDataResult> createResult = weIdService.createWeId();
        logger.info(
            "weIdService is result,errorCode:{},errorMessage:{}",
            createResult.getErrorCode(), createResult.getErrorMessage()
        );

        if (createResult.getErrorCode().intValue() != ErrorCode.SUCCESS.getCode()) {
            return createResult;
        }

        // 3, call set authentication
//        ResponseData<Boolean> setAuthenticateRes = this.setAuthentication(createResult.getResult());
//        if (!setAuthenticateRes.getResult()) {
//            createResult.setErrorCode(
//                ErrorCode.getTypeByErrorCode(setAuthenticateRes.getErrorCode())
//            );
//            return createResult;
//        }
        return createResult;
    }

    /**
     * Set Authentication For WeIdentity DID Document.
     *
     * @param createWeIdDataResult createWeIdDataResult the object of CreateWeIdDataResult
     * @return the response data
     */
    private ResponseData<Boolean> setAuthentication(CreateWeIdDataResult createWeIdDataResult) {

        AuthenticationArgs authenticationArgs = new AuthenticationArgs();
        authenticationArgs.setPublicKey(createWeIdDataResult.getUserWeIdPublicKey().getPublicKey());

        ResponseData<Boolean> setResponse = weIdService.setAuthentication(
                createWeIdDataResult.getWeId(),
                authenticationArgs,
                createWeIdDataResult.getUserWeIdPrivateKey());

        logger.info(
            "setAuthentication is result,errorCode:{},errorMessage:{}",
            setResponse.getErrorCode(), 
            setResponse.getErrorMessage()
        );
        return setResponse;
    }

    /**
     * register on the chain as an authoritative body.
     * 
     * @param authorityName the name of the issue
     * @return true is success, false is failure
     */
    @Override
    public ResponseData<Boolean> registerAuthorityIssuer(String issuer, String authorityName) {

        // build registerAuthorityIssuer parameters.
        AuthorityIssuer authorityIssuerResult = new AuthorityIssuer();
        authorityIssuerResult.setWeId(issuer);
        authorityIssuerResult.setName(authorityName);
        authorityIssuerResult.setAccValue("0");

        RegisterAuthorityIssuerArgs registerAuthorityIssuerArgs = new RegisterAuthorityIssuerArgs();
        registerAuthorityIssuerArgs.setAuthorityIssuer(authorityIssuerResult);
        registerAuthorityIssuerArgs.setWeIdPrivateKey(new WeIdPrivateKey());

        // getting SDK private key from file.
        String privKey = FileUtil.getDataByPath(PrivateKeyUtil.SDK_PRIVKEY_PATH);

        registerAuthorityIssuerArgs.getWeIdPrivateKey().setPrivateKey(privKey);

        ResponseData<Boolean> registResponse =
            authorityIssuerService.registerAuthorityIssuer(registerAuthorityIssuerArgs);
        logger.info(
            "registerAuthorityIssuer is result,errorCode:{},errorMessage:{}",
            registResponse.getErrorCode(), 
            registResponse.getErrorMessage()
        );
        return registResponse;
    }

    /**
     * registered CPT.
     * 
     * @param publisher the weId of the publisher
     * @param privateKey the private key of the publisher
     * @param claim claim is CPT
     * @return returns cptBaseInfo
     */
    @Override
    public ResponseData<CptBaseInfo> registCpt(
        String publisher, 
        String privateKey, 
        Map<String, Object> claim) {

        // build registerCpt parameters.
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(publisher);
        weIdAuthentication.setWeIdPrivateKey(new WeIdPrivateKey());
        weIdAuthentication.getWeIdPrivateKey().setPrivateKey(privateKey);

        CptMapArgs cptMapArgs = new CptMapArgs();
        cptMapArgs.setWeIdAuthentication(weIdAuthentication);
        cptMapArgs.setCptJsonSchema(claim);

        // create CPT by SDK
        ResponseData<CptBaseInfo> response = cptService.registerCpt(cptMapArgs);
        logger.info(
            "registerCpt is result,errorCode:{},errorMessage:{}", 
            response.getErrorCode(),
            response.getErrorMessage()
        );
        return response;
    }

    /**
     * create credential.
     * 
     * @param cptId the cptId of CPT 
     * @param issuer the weId of issue
     * @param privateKey the private key of issuer
     * @param claimDate the data of claim
     * @return returns credential
     */
    @Override
    public ResponseData<CredentialPojo> createCredential(
        Integer cptId, 
        String issuer,
        String privateKey,
        Map<String, Object> claimDate) {

        // build createCredential parameters.
        CreateCredentialPojoArgs<Map<String, Object>> args = new CreateCredentialPojoArgs<>();
        args.setCptId(cptId);
        args.setIssuer(issuer);
        args.setType(CredentialType.ORIGINAL);
        args.setClaim(claimDate);
        // the validity period is 360 days
        args
            .setExpirationDate(System.currentTimeMillis() + EXPIRATION_DATE);
        
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeIdPrivateKey(new WeIdPrivateKey());
        weIdAuthentication.getWeIdPrivateKey().setPrivateKey(privateKey);
        weIdAuthentication.setWeId(issuer);
        weIdAuthentication.setAuthenticationMethodId(issuer);
        args.setWeIdAuthentication(weIdAuthentication);

        // create credentials by SDK.
        ResponseData<CredentialPojo> response = 
            credentialService.createCredential(args);
        logger.info(
            "createCredential is result,errorCode:{},errorMessage:{}",
            response.getErrorCode(), 
            response.getErrorMessage()
        );
        return response;
    }

    /**
     * verifyEvidence credential.
     * 
     * @param credentialJson credentials in JSON format
     * @return returns the result of verifyEvidence
     */
    @Override
    public ResponseData<Boolean> verifyCredential(String credentialJson) {

        ResponseData<Boolean> verifyResponse = null;
        
        CredentialPojo credential = DataToolUtils.deserialize(credentialJson, CredentialPojo.class);
        // verifyEvidence credential on chain.
        verifyResponse = credentialService.verify(credential.getIssuer(), credential);
        logger.info(
            "verifyCredential is result,errorCode:{},errorMessage:{}",
            verifyResponse.getErrorCode(), 
            verifyResponse.getErrorMessage()
        );
        return verifyResponse;
    }

    @Override
    public ResponseData<Boolean> recognizeAuthorityIssuer(String issuer) {
        // getting SDK private key from file.
        String privKey = FileUtil.getDataByPath(PrivateKeyUtil.SDK_PRIVKEY_PATH);
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(privKey);
        ResponseData<Boolean> registResponse =
            authorityIssuerService.recognizeAuthorityIssuer(issuer, weIdPrivateKey);
        logger.info(
            "recognizeAuthorityIssuer is result,errorCode:{},errorMessage:{}",
            registResponse.getErrorCode(), 
            registResponse.getErrorMessage()
        );
        return registResponse;
    }
}
