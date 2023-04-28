
package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.common.model.JsonTransportationSerializeModel;
import com.webank.weid.demo.common.model.JsonTransportationSpecifyModel;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.blockchain.protocol.response.ResponseData;

/**
 * Demo Controller.
 *
 * @author darwindu
 */
@RestController
@Api(description = "传输其他相关接口。",
    tags = {"其他相关接口-Transportation"})
public class DemoOtherTransportationController {

    @Autowired
    private DemoOtherService demoOtherService;

    @ApiOperation(value = "指定transportation的认证者,用于权限控制。")
    @PostMapping("/step1/jsonTransportationSpecify")
    public ResponseData<String> specify(
        @ApiParam(name = "jsonTransportationSpecifyModel", value = "指定transportation的认证者模板")
        @RequestBody JsonTransportationSpecifyModel jsonTransportationSpecifyModel) {
        return demoOtherService.specify(jsonTransportationSpecifyModel);
    }

    @ApiOperation(value = "用于序列化对象,要求对象实现JsonSerializer接口。")
    @PostMapping("/step2/jsonTransportationSerialize")
    public ResponseData<String> serialize(
        @ApiParam(name = "jsonTransportationSerializeModel",
            value = "用于序列化对象,要求对象实现JsonSerializer模板")
        @RequestBody JsonTransportationSerializeModel jsonTransportationSerializeModel) {
        return demoOtherService.serialize(jsonTransportationSerializeModel);
    }
}
