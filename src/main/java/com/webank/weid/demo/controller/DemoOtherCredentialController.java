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

package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.common.model.VerifyCredentialModel;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "电子凭证其他相关接口。",
    tags = {"其他相关接口-Credential"})
public class DemoOtherCredentialController {

    @Autowired
    private DemoOtherService demoOtherService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "传入Credential信息生成Credential整体的Hash值，一般在生成Evidence时调用。")
    @PostMapping("/step1/credential/getCredentialPoJoHash")
    public ResponseData<String> getCredentialHash(
        @ApiParam(name = "credentialModel", value = "电子凭证模板")
        @RequestBody VerifyCredentialModel verifyCredentialModel) {

        return demoOtherService.getCredentialHash(verifyCredentialModel);
    }
}
