package com.webank.weid.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 增加签名接口模板.
 * @author darwindu
 * @date 2020/1/6
 **/
@ApiModel(description = "增加签名接口模板")
public class AddSignatureModel {

    @ApiModelProperty(name = "hashable", value = "实现了Hashable接口的任意Object", required = true,
        example = "0xfbd1d8eed20af617cd5c48972e990adfeca7b694e77bd25e02ae1c23eea3fbec")
    private String hashable;

    @ApiModelProperty(name = "evidenceAddress", value = "存证地址", required = true,
        example = "0x788bfde9ad99376673ed46847294e9b858728045")
    private String evidenceAddress;

    @ApiModelProperty(name = "weid", value = "加签私钥的weid", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String weid;

    public String getHashable() {
        return hashable;
    }

    public void setHashable(String hashable) {
        this.hashable = hashable;
    }

    public String getEvidenceAddress() {
        return evidenceAddress;
    }

    public void setEvidenceAddress(String evidenceAddress) {
        this.evidenceAddress = evidenceAddress;
    }

    public String getWeid() {
        return weid;
    }

    public void setWeid(String weid) {
        this.weid = weid;
    }
}
