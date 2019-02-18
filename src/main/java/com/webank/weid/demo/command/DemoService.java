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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.exception.BusinessException;
import com.webank.weid.protocol.base.AuthorityIssuer;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.CptMapArgs;
import com.webank.weid.protocol.request.CptStringArgs;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.request.RegisterAuthorityIssuerArgs;
import com.webank.weid.protocol.request.SetAuthenticationArgs;
import com.webank.weid.protocol.request.SetPublicKeyArgs;
import com.webank.weid.protocol.request.SetServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.AuthorityIssuerService;
import com.webank.weid.rpc.CptService;
import com.webank.weid.rpc.CredentialService;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.util.JsonUtil;

/**
 * the service for command.
 * @author v_wbgyang
 *
 */
@Component
public class DemoService {

    @Autowired
    private AuthorityIssuerService authorityIssuerService;

    @Autowired
    private CptService cptService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private WeIdService weIdService;

    /**
     * create WeIdentity DID.
     */
    public CreateWeIdDataResult createWeId() throws BusinessException {

        // create WeIdentity DID,publicKey,privateKey
        ResponseData<CreateWeIdDataResult> responseCreate = weIdService.createWeId();
        BaseBean.print("createWeId result:");
        BaseBean.print(responseCreate);
        
        // check result is success
        if (responseCreate.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            throw new BusinessException(responseCreate.getErrorMessage());
        }
        return responseCreate.getResult();
    }

    /**
     * setPublicKey.
     */
    public void setPublicKey(CreateWeIdDataResult createResult, String keyType)
        throws BusinessException {

        // setPublicKey for this WeId
        SetPublicKeyArgs setPublicKeyArgs = new SetPublicKeyArgs();
        setPublicKeyArgs.setWeId(createResult.getWeId());
        setPublicKeyArgs.setPublicKey(createResult.getUserWeIdPublicKey().getPublicKey());
        setPublicKeyArgs.setType(keyType);
        setPublicKeyArgs.setUserWeIdPrivateKey(
            this.buildWeIdPrivateKey(createResult.getUserWeIdPrivateKey().getPrivateKey())
        );
        ResponseData<Boolean> responseSetPub = weIdService.setPublicKey(setPublicKeyArgs);
        BaseBean.print("setPublicKey result:");
        BaseBean.print(responseSetPub);
        
        // check is success
        if (responseSetPub.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || !responseSetPub.getResult()) {
            throw new BusinessException(responseSetPub.getErrorMessage());
        }
    }

    /**
     * setService.
     */
    public void setService(
        CreateWeIdDataResult createResult,
        String serviceType,
        String serviceEnpoint)
        throws BusinessException {

        // setService for this WeIdentity DID
        SetServiceArgs setServiceArgs = new SetServiceArgs();
        setServiceArgs.setWeId(createResult.getWeId());
        setServiceArgs.setType(serviceType);
        setServiceArgs.setServiceEndpoint(serviceEnpoint);
        setServiceArgs.setUserWeIdPrivateKey(
            this.buildWeIdPrivateKey(createResult.getUserWeIdPrivateKey().getPrivateKey())
        );
        ResponseData<Boolean> responseSetSer = weIdService.setService(setServiceArgs);
        BaseBean.print("setService result:");
        BaseBean.print(responseSetSer);
        
        // check is success
        if (responseSetSer.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || !responseSetSer.getResult()) {
            throw new BusinessException(responseSetSer.getErrorMessage());
        }
    }

    /**
     * setAuthenticate.
     */
    public void setAuthenticate(CreateWeIdDataResult createResult, String authType)
        throws BusinessException {

        // setAuthenticate for this WeIdentity DID
        SetAuthenticationArgs setAuthenticationArgs = new SetAuthenticationArgs();
        setAuthenticationArgs.setWeId(createResult.getWeId());
        setAuthenticationArgs.setType(authType);
        setAuthenticationArgs.setPublicKey(createResult.getUserWeIdPublicKey().getPublicKey());
        setAuthenticationArgs.setUserWeIdPrivateKey(
            this.buildWeIdPrivateKey(createResult.getUserWeIdPrivateKey().getPrivateKey())
        );
        
        ResponseData<Boolean> responseSetAuth =
            weIdService.setAuthentication(setAuthenticationArgs);
        BaseBean.print("setAuthentication result:");
        BaseBean.print(responseSetAuth);
        
        // check is success
        if (responseSetAuth.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || !responseSetAuth.getResult()) {
            throw new BusinessException(responseSetAuth.getErrorMessage());
        }
    }

    private WeIdPrivateKey buildWeIdPrivateKey(String privateKey) {
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(privateKey);
        return weIdPrivateKey;
    }
    
    /**
     * getWeIdDom.
     */
    public WeIdDocument getWeIdDom(String weId) throws BusinessException {

        // get weIdDom
        ResponseData<WeIdDocument> responseResult = weIdService.getWeIdDocument(weId);
        BaseBean.print("getWeIdDocument result:");
        BaseBean.print(responseResult);
        
        // check result
        if (responseResult.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || null == responseResult.getResult()) {
            throw new BusinessException(responseResult.getErrorMessage());
        }
        return responseResult.getResult();
    }

    /**
     * regist cpt.
     */
    public CptBaseInfo registCpt(CreateWeIdDataResult weIdResult, String cptJsonSchema)
        throws BusinessException {

        CptStringArgs cptStringArgs = new CptStringArgs();
        cptStringArgs.setWeIdAuthentication(this.buildWeIdAuthentication(weIdResult));
        cptStringArgs.setCptJsonSchema(cptJsonSchema);

        ResponseData<CptBaseInfo> response = cptService.registerCpt(cptStringArgs);
        BaseBean.print("registerCpt result:");
        BaseBean.print(response);
        
        // check result
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || null == response.getResult()) {
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }
    
    /**
     * regist cpt.
     */
    public CptBaseInfo registCpt(
        CreateWeIdDataResult weIdResult,
        Map<String,Object> cptJsonSchemaMap) throws BusinessException {

        CptMapArgs cptMapArgs = new CptMapArgs();
        cptMapArgs.setWeIdAuthentication(this.buildWeIdAuthentication(weIdResult));
        cptMapArgs.setCptJsonSchema(cptJsonSchemaMap);

        ResponseData<CptBaseInfo> responseMap = cptService.registerCpt(cptMapArgs);
        BaseBean.print("registerCpt result:");
        BaseBean.print(responseMap);
        
        // check result
        if (responseMap.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || null == responseMap.getResult()) {
            throw new BusinessException(responseMap.getErrorMessage());
        }
        return responseMap.getResult();
    }
    
    private WeIdAuthentication buildWeIdAuthentication(CreateWeIdDataResult weIdResult) {
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId(weIdResult.getWeId());
        weIdAuthentication.setWeIdPrivateKey(
            this.buildWeIdPrivateKey(weIdResult.getUserWeIdPrivateKey().getPrivateKey())
        );
        return weIdAuthentication;
    }

    /**
     * regist authority issuer.
     */
    public void registerAuthorityIssuer(
        CreateWeIdDataResult weIdResult,
        String name,
        String accValue)
        throws BusinessException {

        AuthorityIssuer authorityIssuerResult = new AuthorityIssuer();
        authorityIssuerResult.setWeId(weIdResult.getWeId());
        authorityIssuerResult.setName(name);
        authorityIssuerResult.setCreated(new Date().getTime());
        authorityIssuerResult.setAccValue(accValue);

        RegisterAuthorityIssuerArgs registerAuthorityIssuerArgs = new RegisterAuthorityIssuerArgs();
        registerAuthorityIssuerArgs.setAuthorityIssuer(authorityIssuerResult);
        registerAuthorityIssuerArgs.setWeIdPrivateKey(
            this.buildWeIdPrivateKey(DemoUtil.SDK_PRIVATE_KEY)
        );

        ResponseData<Boolean> response =
            authorityIssuerService.registerAuthorityIssuer(registerAuthorityIssuerArgs);
        BaseBean.print("registerAuthorityIssuer result:");
        BaseBean.print(response);
        

        // check is success
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode() || !response.getResult()) {
            throw new BusinessException(response.getErrorMessage());
        }
    }

    /**
     * create Credential.
     */
    public Credential createCredential(
        CreateWeIdDataResult weIdResult,
        Integer cptId,
        String claim,
        long expirationDate)
        throws BusinessException {

        Map<String, Object> claimDataMap = (Map<String, Object>) JsonUtil.jsonStrToObj(
                new HashMap<String, Object>(),
                claim);
        
        return this.createCredential(weIdResult, cptId, claimDataMap, expirationDate);
    }
    
    /**
     * create Credential.
     */
    public Credential createCredential(
        CreateWeIdDataResult weIdResult,
        Integer cptId,
        Map<String, Object> claimDataMap,
        long expirationDate)
        throws BusinessException {

        CreateCredentialArgs args = new CreateCredentialArgs();
        args.setClaim(claimDataMap);
        args.setCptId(cptId);
        args.setExpirationDate(expirationDate);
        args.setIssuer(weIdResult.getWeId());
        args.setWeIdPrivateKey(
            this.buildWeIdPrivateKey(weIdResult.getUserWeIdPrivateKey().getPrivateKey())
        );
        
        ResponseData<CredentialWrapper> response = credentialService.createCredential(args);
        BaseBean.print("createCredential result:");
        BaseBean.print(response);
        
        // check result
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || null == response.getResult()) {
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult().getCredential();
    }

    /**
     * verifyCredential.
     */
    public boolean verifyCredential(Credential credential) throws BusinessException {
        
        ResponseData<Boolean> response = credentialService.verify(credential);
        BaseBean.print("verifyCredential result:");
        BaseBean.print(response);
        
        // check is success
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }
}
