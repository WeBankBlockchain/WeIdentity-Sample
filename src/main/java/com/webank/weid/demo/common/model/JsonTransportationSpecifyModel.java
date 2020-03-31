package com.webank.weid.demo.common.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 指定transportation的认证者接口模板.
 * @author darwindu
 * @date 2020/1/6
 **/
@ApiModel(description = "指定transportation的认证者接口模板。")
public class JsonTransportationSpecifyModel {

    @ApiModelProperty(name = "verifierWeIdList", value = "verifierWeId列表", required = true,
        example = "[\"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\"]")
    List<String> verifierWeIdList;

    public List<String> getVerifierWeIdList() {
        return verifierWeIdList;
    }

    public void setVerifierWeIdList(List<String> verifierWeIdList) {
        this.verifierWeIdList = verifierWeIdList;
    }
}
