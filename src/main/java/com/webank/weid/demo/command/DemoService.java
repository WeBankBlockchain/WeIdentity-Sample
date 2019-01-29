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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.demo.exception.BusinessException;
import com.webank.weid.protocol.base.AuthorityIssuer;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.request.RegisterAuthorityIssuerArgs;
import com.webank.weid.protocol.request.RegisterCptArgs;
import com.webank.weid.protocol.request.SetAuthenticationArgs;
import com.webank.weid.protocol.request.SetPublicKeyArgs;
import com.webank.weid.protocol.request.SetServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.AuthorityIssuerService;
import com.webank.weid.rpc.CptService;
import com.webank.weid.rpc.CredentialService;
import com.webank.weid.rpc.WeIdService;

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
        setPublicKeyArgs.setUserWeIdPrivateKey(createResult.getUserWeIdPrivateKey());
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
        setServiceArgs.setUserWeIdPrivateKey(createResult.getUserWeIdPrivateKey());
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
        setAuthenticationArgs.setUserWeIdPrivateKey(createResult.getUserWeIdPrivateKey());
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

        RegisterCptArgs registerCptArgs = new RegisterCptArgs();
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(weIdResult.getUserWeIdPrivateKey().getPrivateKey());
        registerCptArgs.setCptPublisher(weIdResult.getWeId());
        registerCptArgs.setCptPublisherPrivateKey(weIdPrivateKey);
        registerCptArgs.setCptJsonSchema(cptJsonSchema);
        ResponseData<CptBaseInfo> response = cptService.registerCpt(registerCptArgs);
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
        registerAuthorityIssuerArgs.setWeIdPrivateKey(new WeIdPrivateKey());
        registerAuthorityIssuerArgs.getWeIdPrivateKey().setPrivateKey(DemoCommand.PRIVATEKEY);

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

        CreateCredentialArgs args = new CreateCredentialArgs();
        args.setClaim(claim);
        args.setCptId(cptId);
        args.setExpirationDate(expirationDate);
        args.setIssuer(weIdResult.getWeId());
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(weIdResult.getUserWeIdPrivateKey().getPrivateKey());
        args.setWeIdPrivateKey(weIdPrivateKey);
        ResponseData<Credential> response = credentialService.createCredential(args);
        BaseBean.print("createCredential result:");
        BaseBean.print(response);
        
        // check result
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()
            || null == response.getResult()) {
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }

    /**
     * verifyCredential.
     */
    public boolean verifyCredential(Credential credential) throws BusinessException {
        
        ResponseData<Boolean> response = credentialService.verifyCredential(credential);
        BaseBean.print("verifyCredential result:");
        BaseBean.print(response);
        
        // check is success
        if (response.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
            throw new BusinessException(response.getErrorMessage());
        }
        return response.getResult();
    }
}
