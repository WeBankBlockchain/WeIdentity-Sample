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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "User Agent / Credential Repository: "
        + "用户（实体）在此生成WeIdentity DID。为了便于使用，实体也可将自己的私钥、持有的Credential托管于此。",
    tags = {"UserAgent相关接口"})
public class DemoUserAgentController {

    @Autowired
    private DemoService demoService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "创建weid")
    @PostMapping("/step1/userAgent/createWeId")
    public ResponseData<CreateWeIdDataResult> createWeId() {
        return demoService.createWeId();
    }

}
