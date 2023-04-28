
package com.webank.weid.demo.service;

import com.webank.weid.demo.common.model.AddSignatureModel;
import com.webank.weid.demo.common.model.CreateCredentialPojoModel;
import com.webank.weid.demo.common.model.CreateEvidenceModel;
import com.webank.weid.demo.common.model.CreatePresentationModel;
import com.webank.weid.demo.common.model.CreatePresentationPolicyEModel;
import com.webank.weid.demo.common.model.CreateSelectiveCredentialModel;
import com.webank.weid.demo.common.model.CredentialPoJoAddSignature;
import com.webank.weid.demo.common.model.GetCredentialHashModel;
import com.webank.weid.demo.common.model.JsonTransportationSerializeModel;
import com.webank.weid.demo.common.model.JsonTransportationSpecifyModel;
import com.webank.weid.demo.common.model.SetHashValueModel;
import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.common.model.VerifyCredentialPoJoModel;
import com.webank.weid.demo.common.model.VerifyEvidenceModel;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.base.PresentationE;
import com.webank.weid.protocol.base.PresentationPolicyE;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * demo interface.
 * 
 * @author v_wbgyang
 *
 */
public interface DemoOtherService {

    /**
     * get credential hash.
     *
     * @param verifyCredentialModel 验证电子凭证模板
     * @return returns the create hash
     */
    ResponseData<String> getCredentialHash(VerifyCredentialModel verifyCredentialModel);

    /**
     * 创建电子凭证.
     * @param createCredentialPojoModel 创建电子凭证模板
     * @return
     */
    ResponseData<CredentialPojo> createCredentialPoJo(
        CreateCredentialPojoModel createCredentialPojoModel);


    /**
     * 通过原始凭证和披漏策略，创建选择性披露的Credential.
     * @param createSelectiveCredentialModel 创造选择性披露模板
     * @return
     */
    ResponseData<CredentialPojo> createSelectiveCredential(
        CreateSelectiveCredentialModel createSelectiveCredentialModel);

    /**
     * 验证credential.
     * @param verifyCredentialPoJoModel 验证电子凭证模板
     * @return
     */
    ResponseData<Boolean> verify(VerifyCredentialPoJoModel verifyCredentialPoJoModel);


    /**
     * 创建PresentationPolicyEModel.
     * @param createPresentationPolicyEModel 创建PresentationPolicy模板
     * @return
     */
    ResponseData<PresentationPolicyE> createPresentationPolicyE(
        CreatePresentationPolicyEModel createPresentationPolicyEModel);


    /**
     * 获取电子凭证hash.
     * @param getCredentialHashModel 获取电子凭证hash模板
     * @return
     */
    ResponseData<String> getCredentialPoJoHash(GetCredentialHashModel getCredentialHashModel);

    /**
     * 创建Presentation.
     * @param createPresentationModel 创建Presentation模板
     * @return
     */
    ResponseData<PresentationE> createPresentation(
        CreatePresentationModel createPresentationModel);

    /**
     * 多签，在原凭证列表的基础上，创建包裹成一个新的多签凭证，由传入的私钥所签名。此凭证的CPT为一个固定值.
     * 在验证一个多签凭证时，会迭代验证其包裹的所有子凭证。本接口不支持创建选择性披露的多签凭证.
     *
     * @param credentialPoJoAddSignature 电子凭证加签模板
     * @return
     */
    ResponseData<CredentialPojo> addSignatureCredentialPojo(
        CredentialPoJoAddSignature credentialPoJoAddSignature);

    /**
     * 创建存证.
     * @param createEvidenceModel 创建存证模板
     * @return
     */
    ResponseData<String> createEvidence(CreateEvidenceModel createEvidenceModel);


    /**
     * 根据传入的凭证存证地址，在链上查找凭证存证信息.
     * @param evidenceAddress 存证地址
     * @return
     */
    ResponseData<EvidenceInfo> getEvidence(String evidenceAddress);

    /**
     * 指定transportation的认证者,用于权限控制.
     * @param jsonTransportationSpecifyModel 参数模板
     * @return
     */
    ResponseData<String> specify(JsonTransportationSpecifyModel jsonTransportationSpecifyModel);


    /**
     * 用于序列化对象,要求对象实现JsonSerializer接口.
     * @param jsonTransportationSerializeModel 参数模板
     * @return
     */
    ResponseData<String> serialize(
        JsonTransportationSerializeModel jsonTransportationSerializeModel);
}
