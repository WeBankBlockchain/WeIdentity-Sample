package com.webank.weid.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.weid.demo.service.ToolService;
import com.webank.weid.protocol.response.ResponseData;

/**
 * 小工具.
 * @author darwindu
 * @date 2020/1/8
 **/
@RestController
@Api(description = "小工具",
    tags = {"小工具"}, position = 0, hidden = false)
public class ToolController {

    @Autowired
    private ToolService toolService;

    @ApiOperation(value = "通过私钥生成公钥")
    @PostMapping("/step1/getPublicKey")
    public ResponseData<String> getPublicKey(
        @ApiParam(name = "privateKey", value = "私钥",
            example
                = "53079349606873082534274061523339694826923290877435862289172419522326067705985")
        @RequestParam String privateKey) {

        return toolService.getPublicKey(privateKey);
    }
}
