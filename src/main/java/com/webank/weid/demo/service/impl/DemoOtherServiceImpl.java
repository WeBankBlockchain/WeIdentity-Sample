package com.webank.weid.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.webank.weid.kit.transportation.TransportationFactory;
import com.webank.weid.kit.transportation.entity.EncodeType;
import com.webank.weid.kit.transportation.entity.ProtocolProperty;
import com.webank.weid.kit.transportation.entity.TransportationType;
import com.webank.weid.kit.transportation.inf.Transportation;
import com.webank.weid.service.rpc.CredentialPojoService;
import com.webank.weid.service.rpc.EvidenceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.demo.common.model.CreateCredentialPojoModel;
import com.webank.weid.demo.common.model.CreateEvidenceModel;
import com.webank.weid.demo.common.model.CreatePresentationModel;
import com.webank.weid.demo.common.model.CreatePresentationPolicyEModel;
import com.webank.weid.demo.common.model.CreateSelectiveCredentialModel;
import com.webank.weid.demo.common.model.CredentialPoJoAddSignature;
import com.webank.weid.demo.common.model.GetCredentialHashModel;
import com.webank.weid.demo.common.model.JsonTransportationSerializeModel;
import com.webank.weid.demo.common.model.JsonTransportationSpecifyModel;
import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.common.model.VerifyCredentialPoJoModel;
import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.protocol.base.Challenge;
import com.webank.weid.protocol.base.ClaimPolicy;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.base.HashString;
import com.webank.weid.protocol.base.PresentationE;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.inf.Hashable;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.EvidenceServiceImpl;
import com.webank.weid.util.DataToolUtils;

/**
 * other interface.
 *
 * @author darwindu
 * @date 2020/1/3
 **/
@Service
public class DemoOtherServiceImpl implements DemoOtherService {

    static {
        FileUtil.loadConfigFromEnv();
    }

    private static final Logger logger = LoggerFactory.getLogger(DemoOtherServiceImpl.class);

    private CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();

    private EvidenceService evidenceService = new EvidenceServiceImpl();

    @Override
    public ResponseData<String> getCredentialHash(VerifyCredentialModel verifyCredentialModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("verifyCredentialModel:{}",
                DataToolUtils.objToJsonStrWithNoPretty(verifyCredentialModel));
            if (null == verifyCredentialModel || null == verifyCredentialModel.getCredential()) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            String credentialJson
                = DataToolUtils.mapToCompactJson(verifyCredentialModel.getCredential());
            CredentialPojo credential = DataToolUtils.deserialize(credentialJson, 
                CredentialPojo.class);
            ResponseData<String> responseData = credentialPojoService.getCredentialPojoHash(
                credential);
            logger.info("{} responseData: {}",
                methodName, DataToolUtils.objToJsonStrWithNoPretty(responseData));
            return responseData;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<CredentialPojo> createCredentialPoJo(
        CreateCredentialPojoModel createCredentialPojoModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("{} createCredentialPojoModel: {}", methodName,
            DataToolUtils.objToJsonStrWithNoPretty(createCredentialPojoModel));

        if (null == createCredentialPojoModel
            || StringUtils.isBlank(createCredentialPojoModel.getIssuer())
            || null == createCredentialPojoModel.getCptId()
            || null == createCredentialPojoModel.getClaimData()
            || createCredentialPojoModel.getClaimData().isEmpty()
        ) {
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }

        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs
            = new CreateCredentialPojoArgs<>();
        createCredentialPojoArgs.setCptId(createCredentialPojoModel.getCptId());
        createCredentialPojoArgs.setIssuer(createCredentialPojoModel.getIssuer());
        createCredentialPojoArgs.setExpirationDate(
            System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 100);

        WeIdAuthentication weIdAuthentication =
            getWeIdAuthentication(createCredentialPojoModel.getIssuer());
        createCredentialPojoArgs.setWeIdAuthentication(weIdAuthentication);

        createCredentialPojoArgs.setClaim(createCredentialPojoModel.getClaimData());

        ResponseData<CredentialPojo> responseData
            = credentialPojoService.createCredential(createCredentialPojoArgs);
        logger.info("{} responseData:{}",
            methodName, DataToolUtils.objToJsonStrWithNoPretty(responseData));
        return responseData;
    }

    @Override
    public ResponseData<CredentialPojo> createSelectiveCredential(
        CreateSelectiveCredentialModel createSelectiveCredentialModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("{} createSelectiveCredentialModel: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(createSelectiveCredentialModel));

            if (null == createSelectiveCredentialModel
                || null == createSelectiveCredentialModel.getCredential()
                || createSelectiveCredentialModel.getCredential().isEmpty()
                || null == createSelectiveCredentialModel.getFieldsToBeDisclosed()
                || createSelectiveCredentialModel.getFieldsToBeDisclosed().isEmpty()
            ) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            CredentialPojo credentialPojo = DataToolUtils.mapToObj(
                createSelectiveCredentialModel.getCredential(), CredentialPojo.class);

            // 选择性披露
            ClaimPolicy claimPolicy = new ClaimPolicy();
            claimPolicy.setFieldsToBeDisclosed(
                DataToolUtils.objToJsonStrWithNoPretty(
                    createSelectiveCredentialModel.getFieldsToBeDisclosed()));

            ResponseData<CredentialPojo> selectiveResponse =
                credentialPojoService.createSelectiveCredential(credentialPojo, claimPolicy);
            logger.info("{} selectiveResponse: {}",
                methodName, DataToolUtils.objToJsonStrWithNoPretty(selectiveResponse));
            return selectiveResponse;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<Boolean> verify(VerifyCredentialPoJoModel verifyCredentialPoJoModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("{} verifyCredentialPoJoModel: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(verifyCredentialPoJoModel));

            if (null == verifyCredentialPoJoModel
                || StringUtils.isBlank(verifyCredentialPoJoModel.getIssuerWeId())
                || null == verifyCredentialPoJoModel.getCredential()
                || verifyCredentialPoJoModel.getCredential().isEmpty()) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            CredentialPojo credentialPojo = DataToolUtils.mapToObj(
                verifyCredentialPoJoModel.getCredential(), CredentialPojo.class);

            ResponseData<Boolean> responseVerify = credentialPojoService.verify(
                verifyCredentialPoJoModel.getIssuerWeId(), credentialPojo);
            logger.info("{} responseVerify: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(responseVerify));
            return responseVerify;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<PresentationPolicyE> createPresentationPolicyE(
        CreatePresentationPolicyEModel createPresentationPolicyEModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("{} verifyCredentialPoJoModel: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(createPresentationPolicyEModel));
            if (null == createPresentationPolicyEModel
                || createPresentationPolicyEModel.getPresentationPolicyE().isEmpty()) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            //创建PresentationPolicyE
            PresentationPolicyE presentationPolicyE
                = PresentationPolicyE.fromJson(
                DataToolUtils.mapToCompactJson(
                    createPresentationPolicyEModel.getPresentationPolicyE()));
            logger.info("{} presentationPolicyE: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(presentationPolicyE));
            return new ResponseData<>(presentationPolicyE, ErrorCode.SUCCESS);
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<String> getCredentialPoJoHash(
        GetCredentialHashModel getCredentialHashModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("{} getCredentialHashModel: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(getCredentialHashModel));
            if (null == getCredentialHashModel.getCredential()
                || getCredentialHashModel.getCredential().isEmpty()) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            CredentialPojo credentialPojo =
                DataToolUtils.mapToObj(
                    getCredentialHashModel.getCredential(), CredentialPojo.class);
            ResponseData<String> responseData
                = credentialPojoService.getCredentialPojoHash(credentialPojo);
            logger.info("{} responseData: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(responseData));
            return responseData;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<PresentationE> createPresentation(
        CreatePresentationModel createPresentationModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("{} createPresentationModel: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(createPresentationModel));

            if (null == createPresentationModel
                || null == createPresentationModel.getCredentialList()
                || createPresentationModel.getCredentialList().isEmpty()
                || StringUtils.isBlank(createPresentationModel.getChallengeUserWeId())
                || null == createPresentationModel.getPresentationPolicyE()
                || createPresentationModel.getPresentationPolicyE().isEmpty()
                || StringUtils.isBlank(createPresentationModel.getWeId())) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            List<CredentialPojo> credentialList = new ArrayList<CredentialPojo>();
            for (Map<String, Object> map : createPresentationModel.getCredentialList()) {
                CredentialPojo credentialPojo = DataToolUtils.mapToObj(map, CredentialPojo.class);
                credentialList.add(credentialPojo);
            }

            //创建Challenge
            Challenge challenge = Challenge.create(
                createPresentationModel.getChallengeUserWeId(),
                String.valueOf(System.currentTimeMillis()));

            //创建PresentationPolicyE
            PresentationPolicyE presentationPolicyE
                = PresentationPolicyE.fromJson(
                    DataToolUtils.mapToCompactJson(
                        createPresentationModel.getPresentationPolicyE()));

            WeIdAuthentication weIdAuthentication
                = getWeIdAuthentication(createPresentationModel.getWeId());

            //创建Presentation
            ResponseData<PresentationE>  presentationE =
                credentialPojoService.createPresentation(
                    credentialList, presentationPolicyE, challenge, weIdAuthentication);
            logger.info("{} presentationE: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(presentationE));
            return presentationE;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<CredentialPojo> addSignatureCredentialPojo(
        CredentialPoJoAddSignature credentialPoJoAddSignature) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            logger.info("credentialPoJoAddSignature:{}",
                DataToolUtils.objToJsonStrWithNoPretty(credentialPoJoAddSignature));
            if (null == credentialPoJoAddSignature
                || null == credentialPoJoAddSignature.getCredentialList()
                || 0 == credentialPoJoAddSignature.getCredentialList().size()
                || StringUtils.isBlank(credentialPoJoAddSignature.getWeid())) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            List<CredentialPojo> credentialList = new ArrayList<CredentialPojo>();
            for (Map<String, Object> map : credentialPoJoAddSignature.getCredentialList()) {
                CredentialPojo credentialPojo = DataToolUtils.mapToObj(map, CredentialPojo.class);
                credentialList.add(credentialPojo);
            }

            ResponseData<CredentialPojo> responseData = credentialPojoService.addSignature(
                credentialList,
                getWeIdAuthentication(credentialPoJoAddSignature.getWeid()));
            logger.info("{} responseData: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(responseData));
            return responseData;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    @Override
    public ResponseData<String> createEvidence(CreateEvidenceModel createEvidenceModel) {

        logger.info("createEvidenceModel:{}",
            DataToolUtils.objToJsonStrWithNoPretty(createEvidenceModel));
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        if (null == createEvidenceModel
            || StringUtils.isBlank(createEvidenceModel.getHashable())
            || StringUtils.isBlank(createEvidenceModel.getWeid())) {
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }
        // Hashable为接口，创建Hashable可以new实现类，例如：credentail
        Hashable hashable = new HashString(createEvidenceModel.getHashable());

        ResponseData<String> responseData = evidenceService.createEvidence(hashable,
            getWeIdPrivateKey(createEvidenceModel.getWeid()));
        logger.info("{} responseData: {}", methodName,
            DataToolUtils.objToJsonStrWithNoPretty(responseData));
        return responseData;
    }

    @Override
    public ResponseData<EvidenceInfo> getEvidence(String evidenceAddress) {

        logger.info("evidenceAddress:{}",
            DataToolUtils.objToJsonStrWithNoPretty(evidenceAddress));
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ResponseData<EvidenceInfo> responseData = evidenceService.getEvidence(evidenceAddress);
        logger.info("{} responseData: {}", methodName,
            DataToolUtils.objToJsonStrWithNoPretty(responseData));
        return responseData;
    }

    @Override
    public ResponseData<String> specify(
        JsonTransportationSpecifyModel jsonTransportationSpecifyModel) {

        logger.info("jsonTransportationSpecifyModel:{}", DataToolUtils.objToJsonStrWithNoPretty(
            jsonTransportationSpecifyModel));
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        if (null == jsonTransportationSpecifyModel
            || null == jsonTransportationSpecifyModel.getVerifierWeIdList()
            || 0 == jsonTransportationSpecifyModel.getVerifierWeIdList().size()) {
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }

        Transportation transportation = TransportationFactory.build(TransportationType.JSON)
            .specify(jsonTransportationSpecifyModel.getVerifierWeIdList());
        logger.info("{} jsonTransportation: {}", methodName,
            DataToolUtils.objToJsonStrWithNoPretty(transportation));
        ResponseData<String> responseData = new ResponseData<>();
        responseData.setResult(DataToolUtils.objToJsonStrWithNoPretty(transportation));
        return responseData;
    }

    @Override
    public ResponseData<String> serialize(
        JsonTransportationSerializeModel jsonTransportationSerializeModel) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        try {
            logger.info("jsonTransportationSerializeModel:{}",
                DataToolUtils.objToJsonStrWithNoPretty(jsonTransportationSerializeModel));

            if (null == jsonTransportationSerializeModel
                || null == jsonTransportationSerializeModel.getVerifierWeIdList()
                || 0 == jsonTransportationSerializeModel.getVerifierWeIdList().size()
                || StringUtils.isBlank(jsonTransportationSerializeModel.getEncodeType())
                || null == jsonTransportationSerializeModel.getPresentationE()
                || jsonTransportationSerializeModel.getPresentationE().isEmpty()) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }

            PresentationE presentation
                = DataToolUtils.mapToObj(
                jsonTransportationSerializeModel.getPresentationE(), PresentationE.class);

            //原文方式调用
            com.webank.weid.kit.protocol.response.ResponseData<String> responseData =
                TransportationFactory
                    .newJsonTransportation()
                    .specify(jsonTransportationSerializeModel.getVerifierWeIdList())
                    .serialize(
                        presentation,
                        new ProtocolProperty(
                            EncodeType.getEncodeType(
                                Integer.valueOf(
                                    jsonTransportationSerializeModel.getEncodeType()))));
            logger.info("{} responseData: {}", methodName,
                DataToolUtils.objToJsonStrWithNoPretty(responseData));
            ResponseData newResponseData = new ResponseData();
            newResponseData.setResult(responseData.getResult());
            newResponseData.setErrorCode(ErrorCode.getTypeByErrorCode(responseData.getErrorCode()));
            newResponseData.setErrorMessage(responseData.getErrorMessage());
            return newResponseData;
        } catch (Exception e) {
            logger.error("{} error", methodName, e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    private WeIdPrivateKey getWeIdPrivateKey(String weid) {
        String privateKey = PrivateKeyUtil
            .getPrivateKeyByWeId(PrivateKeyUtil.KEY_DIR, weid);
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(privateKey);
        return weIdPrivateKey;
    }

    private WeIdAuthentication getWeIdAuthentication(String weid) {

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(weid);
        WeIdPrivateKey weIdPrivateKey = getWeIdPrivateKey(weid);
        weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey);
        weIdAuthentication.setAuthenticationMethodId(weid + "#key0");
        return weIdAuthentication;
    }
}
