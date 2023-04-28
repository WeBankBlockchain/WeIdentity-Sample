
package com.webank.weid.demo.command;

import java.util.List;
import java.util.Map;

import com.webank.weid.kit.transportation.TransportationFactory;
import com.webank.weid.kit.transportation.entity.EncodeType;
import com.webank.weid.kit.transportation.entity.ProtocolProperty;
import com.webank.weid.protocol.base.*;
import com.webank.weid.protocol.request.*;
import com.webank.weid.service.rpc.AuthorityIssuerService;
import com.webank.weid.service.rpc.CptService;
import com.webank.weid.service.rpc.CredentialPojoService;
import com.webank.weid.service.rpc.WeIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.demo.exception.BusinessException;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.service.impl.AuthorityIssuerServiceImpl;
import com.webank.weid.service.impl.CptServiceImpl;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;

/**
 * the service for command.
 * @author v_wbgyang
 *
 */
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    private AuthorityIssuerService authorityIssuerService = new AuthorityIssuerServiceImpl();

    private CptService cptService = new CptServiceImpl();

    private WeIdService weIdService = new WeIdServiceImpl();

    private CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();

    /**
     * Create a WeIdentity DID with null input param.
     *
     * @return the result of createWeId
     */
    public CreateWeIdDataResult createWeId() throws BusinessException {

        // create WeIdentity DID,publicKey,privateKey
        ResponseData<CreateWeIdDataResult> responseCreate = weIdService.createWeId();
        BaseBean.print("createWeId result:");
        BaseBean.print(responseCreate);

        // throw an exception if it does not succeed.
        if (responseCreate.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call createWeId method, code={}, message={}",
                    responseCreate.getErrorCode(),
                    responseCreate.getErrorMessage()
            );
            throw new BusinessException(responseCreate.getErrorMessage());
        }
        return responseCreate.getResult();
    }

    /**
     * Set Public Key For WeIdentity DID Document.
     *
     * @param createResult the Object of CreateWeIdDataResult
     */
//    public void addPublicKey(CreateWeIdDataResult createResult)
//            throws BusinessException {
//
//        PublicKeyArgs publicKeyArgs = new PublicKeyArgs();
//        publicKeyArgs.setPublicKey(createResult.getUserWeIdPublicKey().getPublicKey());
//        ResponseData<Integer> responseSetPub = weIdService.addPublicKey(
//                createResult.getWeId(), publicKeyArgs, createResult.getUserWeIdPrivateKey());
//
//        BaseBean.print("setPublicKey result:");
//        BaseBean.print(responseSetPub);
//
//        if (responseSetPub.getErrorCode() != ErrorCode.SUCCESS.getCode()
//                && responseSetPub.getErrorCode()
//                == ErrorCode.WEID_PUBLIC_KEY_ALREADY_EXISTS.getCode()) {
//            logger.info("the publicKey already exist!");
//        } else if (responseSetPub.getErrorCode() != ErrorCode.SUCCESS.getCode()
//                || responseSetPub.getResult() == -1) {
//            logger.error("failed to call setPublicKey method, code={}, message={}",
//                    responseSetPub.getErrorCode(),
//                    responseSetPub.getErrorMessage()
//            );
//            throw new BusinessException(responseSetPub.getErrorMessage());
//        }
//    }

    /**
     * Create a WeIdentity DID from the provided public key.
     * A private key is required to send transaction, but may be not matching the given public key
     *
     * @param weIdPublicKey  you need to input a public key
     * @param weIdPrivateKey you need to input a private key
     * @return a data set including a WeIdentity DID and a keypair
     */
    public String createWeIdByPublicKey(WeIdPublicKey weIdPublicKey, WeIdPrivateKey weIdPrivateKey) throws BusinessException {
        ResponseData<String> response = weIdService.createWeIdByPublicKey(weIdPublicKey, weIdPrivateKey);
        logger.info("createWeId", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call createWeIdByPublicKey, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * Create a WeIdentity DID from the provided public key.
     *
     * @param createWeIdArgs the create WeIdentity DID args
     * @return WeIdentity DID
     */
    public String createWeId(CreateWeIdArgs createWeIdArgs) throws BusinessException {

        ResponseData<String> response = weIdService.createWeId(createWeIdArgs);
        // throw an exception if it does not succeed.
        if (response.getErrorCode().intValue() != ErrorCode.WEID_ALREADY_EXIST.getCode()
                && response.getErrorCode().intValue() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call createWeId method, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    public String getWeIdDocumentJson(String weId) throws BusinessException {
        ResponseData<String> response =
                weIdService.getWeIdDocumentJson(weId);
        logger.info("getWeIdDocumentJson", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call getWeIdDocumentJson, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * Set service properties.
     *
     * @param weId        the WeID to set service to
     * @param serviceArgs your service name and endpoint
     * @param privateKey  the private key
     * @return true if the "set" operation succeeds, false otherwise.
     */
    public void setService(
            String weId, ServiceArgs serviceArgs,
            WeIdPrivateKey privateKey)
            throws BusinessException {

        ResponseData<Boolean> response = weIdService.setService(weId,
                serviceArgs, privateKey);

        // throw an exception if it does not succeed.
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || !response.getResult()) {
            logger.error("failed to call setService method, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
    }

    /**
     * Set Authentication for WeIdentity DID Document.
     *
     * @param createResult the Object of CreateWeIdDataResult
     */
    public void setAuthentication(CreateWeIdDataResult createResult)
            throws BusinessException {

        AuthenticationArgs authenticationArgs = new AuthenticationArgs();
        authenticationArgs.setPublicKey(createResult.getUserWeIdPublicKey().getPublicKey());

        ResponseData<Boolean> responseSetAuth = weIdService.setAuthentication(
                createResult.getWeId(),
                authenticationArgs,
                createResult.getUserWeIdPrivateKey());

        BaseBean.print("setAuthentication result:");
        BaseBean.print(responseSetAuth);

        // throw an exception if it does not succeed.
        if (responseSetAuth.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || !responseSetAuth.getResult()) {
            logger.error("failed to call setAuthentication method, code={}, message={}",
                    responseSetAuth.getErrorCode(),
                    responseSetAuth.getErrorMessage()
            );
            throw new BusinessException(responseSetAuth.getErrorMessage());
        }
    }

    /**
     * Query WeIdentity DID document metadata.
     *
     * @param weId the WeIdentity DID
     * @return weId document metadata in java object type
     */
    public WeIdDocumentMetadata getWeIdDocumentMetadata(String weId){
        ResponseData<WeIdDocumentMetadata> response = weIdService.getWeIdDocumentMetadata(weId);
        logger.info("getWeIdDocumentMetadata", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call getWeIdDocumentMetadata, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * Check if the WeIdentity DID exists on chain.
     *
     * @param weId The WeIdentity DID.
     * @return true if exists, false otherwise.
     */
    public boolean isWeIdExist(String weId) throws BusinessException {
        ResponseData<Boolean> response = weIdService.isWeIdExist(weId);
        logger.info("isWeIdExist", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call isWeIdExist, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();

    }

    /**
     * Check if the WeIdentity DID is deactivated on chain.
     *
     * @param weId The WeIdentity DID.
     * @return true if is deactivated, false otherwise.
     */
    public boolean isDeactivated(String weId) throws BusinessException {
        ResponseData<Boolean> response = weIdService.isDeactivated(weId);
        logger.info("isDeactivated", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call isDeactivated, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * Remove an authentication tag in WeID document only - will not affect its public key.
     *
     * @param weId               the WeID to remove auth from
     * @param authenticationArgs A public key is needed
     * @param privateKey         the private key
     * @return true if succeeds, false otherwise
     */
    public boolean revokeAuthentication(
            String weId, AuthenticationArgs authenticationArgs,
            WeIdPrivateKey privateKey) throws BusinessException {
        ResponseData<Boolean> response = weIdService.revokeAuthentication(weId, authenticationArgs, privateKey);
        logger.info("revokeAuthentication", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call revokeAuthentication, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * query data according to block height, index location and search direction.
     *
     * @param first the first index of weid in contract
     * @param last the last index of weid in contract
     * @return return the WeId List
     */
    public List<String> getWeIdList(
            Integer first,
            Integer last
    ) {
        ResponseData<List<String>> response = weIdService.getWeIdList(first, last);
        logger.info("getWeIdList", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call getWeIdList, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * get total weId.
     *
     * @return total weid
     */
    public Integer getWeIdCount() {
        ResponseData<Integer> response = weIdService.getWeIdCount();
        logger.info("getWeIdCount", response);

        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("failed to call getWeIdCount, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * Build WeIdPrivateKey object.
     *
     * @param privateKey private key
     * @return return WeIdPrivateKey object
     */
    private WeIdPrivateKey buildWeIdPrivateKey(String privateKey) {
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(privateKey);
        return weIdPrivateKey;
    }

    /**
     * Get a WeIdentity DID Document.
     *
     * @param weId the WeIdentity DID
     * @return the WeIdentity DID document
     */
    public WeIdDocument getWeIdDom(String weId) throws BusinessException {

        // getting weIdDOM by weId on chain.
        ResponseData<WeIdDocument> responseResult = weIdService.getWeIdDocument(weId);
        BaseBean.print("getWeIdDocument result:");
        BaseBean.print(responseResult);

        // throw an exception if it does not succeed.
        if (responseResult.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || null == responseResult.getResult()) {
            logger.error("failed to call getWeIdDocument method, code={}, message={}",
                    responseResult.getErrorCode(),
                    responseResult.getErrorMessage()
            );
            throw new BusinessException(responseResult.getErrorMessage());
        }
        return responseResult.getResult();
    }

    /**
     * register a new CPT on chain.
     *
     * @param weIdResult weId information containing private keys
     * @param cptJsonSchema the String data is a JSON schema for registered CPT
     * @return the data of CPT Base Information
     * @throws BusinessException throw a exception when register fail
     */
    public CptBaseInfo registCpt(
            CreateWeIdDataResult weIdResult,
            String cptJsonSchema,
            Integer cptId
    ) throws BusinessException {

        logger.info("regist CPT with CptStringArgs");

        // build CptStringArgs for registerCpt.
        CptStringArgs cptStringArgs = new CptStringArgs();
        cptStringArgs.setWeIdAuthentication(this.buildWeIdAuthentication(weIdResult));
        cptStringArgs.setCptJsonSchema(cptJsonSchema);

        // call the registerCpt on chain.
        ResponseData<CptBaseInfo> response = cptService.registerCpt(cptStringArgs, cptId);
        BaseBean.print("registerCpt result:");
        BaseBean.print(response);

        // throw an exception if it does not succeed.
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || null == response.getResult()) {

            if (response.getErrorCode() == ErrorCode.CPT_ALREADY_EXIST.getCode()) {
                logger.info("The cpt is already register");
            } else {
                logger.error("failed to call registerCpt method, code={}, message={}",
                        response.getErrorCode(),
                        response.getErrorMessage()
                );
                throw new BusinessException(response.getErrorMessage());
            }
        }
        return response.getResult();
    }

    /**
     * register a new CPT on chain.
     *
     * @param weIdResult weId information containing private keys
     * @param cptJsonSchemaMap schema of map type
     * @return return CPT information
     * @throws BusinessException throw a exception when register fail
     */
    public CptBaseInfo registCpt(
            CreateWeIdDataResult weIdResult,
            Map<String,Object> cptJsonSchemaMap)
            throws BusinessException {

        logger.info("regist CPT with CptMapArgs");

        // build CptMapArgs for registerCpt.
        CptMapArgs cptMapArgs = new CptMapArgs();
        cptMapArgs.setWeIdAuthentication(this.buildWeIdAuthentication(weIdResult));
        cptMapArgs.setCptJsonSchema(cptJsonSchemaMap);

        // call the registerCpt on chain.
        ResponseData<CptBaseInfo> responseMap = cptService.registerCpt(cptMapArgs);
        BaseBean.print("registerCpt result:");
        BaseBean.print(responseMap);

        // throw an exception if it does not succeed.
        if (responseMap.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || null == responseMap.getResult()) {
            logger.error("failed to call registerCpt method, code={}, message={}",
                    responseMap.getErrorCode(),
                    responseMap.getErrorMessage()
            );
            throw new BusinessException(responseMap.getErrorMessage());
        }
        return responseMap.getResult();
    }

    /**
     * build WeIdAuthentication object by CreateWeIdDataResult object.
     *
     * @param weIdResult CreateWeIdDataResult object
     * @return return WeIdAuthentication object
     */
    private WeIdAuthentication buildWeIdAuthentication(CreateWeIdDataResult weIdResult) {
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(weIdResult.getWeId());
        weIdAuthentication.setWeIdPrivateKey(
                this.buildWeIdPrivateKey(weIdResult.getUserWeIdPrivateKey().getPrivateKey())
        );
        return weIdAuthentication;
    }

    /**
     * Register a new Authority Issuer on Chain.
     *
     * @param weIdResult the object of CreateWeIdDataResult
     * @param name this is Authority name
     * @param accValue this is accValue
     * @throws BusinessException throw a exception when register fail
     */
    public void registerAuthorityIssuer(
            CreateWeIdDataResult weIdResult,
            String name,
            String accValue)
            throws BusinessException {

        // build AuthorityIssuer object.
        AuthorityIssuer authorityIssuerResult = new AuthorityIssuer();
        authorityIssuerResult.setWeId(weIdResult.getWeId());
        authorityIssuerResult.setName(name);
        authorityIssuerResult.setAccValue(accValue);

        // build RegisterAuthorityIssuerArgs for registerAuthorityIssuer.
        RegisterAuthorityIssuerArgs registerAuthorityIssuerArgs = new RegisterAuthorityIssuerArgs();
        registerAuthorityIssuerArgs.setAuthorityIssuer(authorityIssuerResult);
        registerAuthorityIssuerArgs.setWeIdPrivateKey(
                this.buildWeIdPrivateKey(DemoUtil.SDK_PRIVATE_KEY)
        );

        // call the registerAuthorityIssuer on chain.
        ResponseData<Boolean> response =
                authorityIssuerService.registerAuthorityIssuer(registerAuthorityIssuerArgs);
        BaseBean.print("registerAuthorityIssuer result:");
        BaseBean.print(response);

        // throw an exception if it does not succeed.
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || !response.getResult()) {
            logger.error("failed to call registerAuthorityIssuer method, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
    }

    /**
     * Register a new Authority Issuer on Chain.
     *
     * @param weIdResult the object of CreateWeIdDataResult
     * @throws BusinessException throw a exception when register fail
     */
    public void recognizeAuthorityIssuer(CreateWeIdDataResult weIdResult) throws BusinessException {
        WeIdPrivateKey weIdPrivateKey = this.buildWeIdPrivateKey(DemoUtil.SDK_PRIVATE_KEY);
        // call the registerAuthorityIssuer on chain.
        ResponseData<Boolean> response =
                authorityIssuerService.recognizeAuthorityIssuer(weIdResult.getWeId(), weIdPrivateKey);
        BaseBean.print("recognizeAuthorityIssuer result:");
        BaseBean.print(response);

        // throw an exception if it does not succeed.
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
                || !response.getResult()) {
            logger.error("failed to call recognizeAuthorityIssuer method, code={}, message={}",
                    response.getErrorCode(),
                    response.getErrorMessage()
            );
            throw new BusinessException(response.getErrorMessage());
        }
    }

    /**
     * create a credential for user.
     * @param arg the CreateCredentialPojoArgs
     * @return
     */
    public <T> CredentialPojo createCredential(CreateCredentialPojoArgs<T> arg) {
        ResponseData<CredentialPojo> response =
                credentialPojoService.createCredential(arg);
        BaseBean.print("createCredential result:");
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("credentialPojoService.createCredential failed,error:{}",response);
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     *  create presentation with policy.
     * @param credentialList the credentialList of user
     * @param presentationPolicyE the policy for create selective credential
     * @param challenge the challenge form verifier
     * @param createWeId the weId information of user
     * @return the object of PresentationE
     */
    public PresentationE createPresentation(
            List<CredentialPojo> credentialList,
            PresentationPolicyE presentationPolicyE,
            Challenge challenge,
            CreateWeIdDataResult createWeId) {

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(createWeId.getWeId());
        weIdAuthentication.setAuthenticationMethodId(createWeId.getWeId() + "#keys-0");
        weIdAuthentication.setWeIdPrivateKey(createWeId.getUserWeIdPrivateKey());
        ResponseData<PresentationE> response =
                credentialPojoService.createPresentation(
                        credentialList,
                        presentationPolicyE,
                        challenge,
                        weIdAuthentication
                );
        BaseBean.print("createPresentation result:");
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("credentialPojoService.createPresentation failed,error:{}",response);
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * serialize presentation to String by JsonTransportation.
     * @param weIds specifyWeIds to verifier
     * @param presentationE the presentationE
     * @return serialize string
     */
    public String presentationEToJson(List<String> weIds, PresentationE presentationE) {

        com.webank.weid.kit.protocol.response.ResponseData<String> response =
                TransportationFactory
                        .newJsonTransportation()
                        .specify(weIds)
                        .serialize(presentationE,new ProtocolProperty(EncodeType.ORIGINAL));
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error(
                    "jsonTransportation.serialize failed,responseData:{}",
                    response
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * serialize presentation to String by QrCodeTransportation.
     * @param weIds specifyWeIds to verifier
     * @param presentationE the presentationE
     * @return serialize string
     */
    public String presentationEToQrCode(List<String> weIds, PresentationE presentationE) {

        com.webank.weid.kit.protocol.response.ResponseData<String> response =
                TransportationFactory
                        .newQrCodeTransportation()
                        .specify(weIds)
                        .serialize(presentationE,new ProtocolProperty(EncodeType.ORIGINAL));
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error(
                    "QRCodeTransportation.serialize failed,responseData:{}",
                    response
            );
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * deserialize transString by JsonTransportation.
     * @param presentationJson JSON String
     * @param createWeId the weId information of verifier
     * @return PresentationE
     */
    public PresentationE deserializePresentationJson(
            String presentationJson,
            CreateWeIdDataResult createWeId) {

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(createWeId.getWeId());
        weIdAuthentication.setAuthenticationMethodId(createWeId.getWeId() + "#keys-0");
        weIdAuthentication.setWeIdPrivateKey(createWeId.getUserWeIdPrivateKey());

        com.webank.weid.kit.protocol.response.ResponseData<PresentationE> response =
                TransportationFactory
                        .newJsonTransportation()
                        .deserialize(weIdAuthentication, presentationJson, PresentationE.class);
        BaseBean.print("JsonTransportation.deserialize result:");
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("jsonTransportation.deserialize failed,responseData:{}",
                    response);
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * deserialize transString by QrCodeTransportation.
     * @param presentationJson QrCode String
     * @param createWeId the weId information of verifier
     * @return PresentationE
     */
    public PresentationE deserializePresentationQrCode(
            String presentationJson,
            CreateWeIdDataResult createWeId) {

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(createWeId.getWeId());
        weIdAuthentication.setAuthenticationMethodId(createWeId.getWeId() + "#keys-0");
        weIdAuthentication.setWeIdPrivateKey(createWeId.getUserWeIdPrivateKey());

        com.webank.weid.kit.protocol.response.ResponseData<PresentationE> response =
                TransportationFactory
                        .newQrCodeTransportation()
                        .deserialize(weIdAuthentication, presentationJson, PresentationE.class);
        BaseBean.print("QrCodeTransportation.deserialize result:");
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("QrCodeTransportation.deserialize failed,responseData:{}",
                    response);
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * verify the presentation.
     * @param weId the weId of userAgent
     * @param presentationPolicyE the policy of verifier
     * @param challenge the challenge of verifier
     * @param presentationE the presentation of userAgent
     * @return success if true, others fail
     */
    public boolean verifyPresentationE(
            String weId,
            PresentationPolicyE presentationPolicyE,
            Challenge challenge,
            PresentationE presentationE) {

        ResponseData<Boolean> response =
                credentialPojoService.verify(weId, presentationPolicyE, challenge, presentationE);
        BaseBean.print("verify result:");
        BaseBean.print(response);
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("credentialPojoService.verify failed,responseData:{}",
                    response);
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     *  verify CredentialPojo.
     * @param credentialPojo the CredentialPojo
     * @return success if true, others fail
     */
    public boolean verifyCredentialPojo(CredentialPojo credentialPojo) {
        ResponseData<Boolean> verify =
                credentialPojoService.verify(credentialPojo.getIssuer(), credentialPojo);
        if (verify.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            logger.error("credentialPojoService.verify failed,responseData:{}",
                    verify);
            throw new BusinessException(verify.getErrorMessage());
        }
        return verify.getResult();
    }

    /**
     * get the public key ID.
     * @param weId the weId
     * @return the publicKeyId
     */
//    public String getPublicKeyId(String weId) {
//        ResponseData<WeIdDocument> weIdDocumentRes = weIdService.getWeIdDocument(weId);
//        String publicKeyId = null;
//        for (PublicKeyProperty publicKey : weIdDocumentRes.getResult().getPublicKey()) {
//            if (publicKey.getOwner().equals(weId)) {
//                publicKeyId = publicKey.getId();
//                break;
//            }
//        }
//        return publicKeyId;
//    }

}
