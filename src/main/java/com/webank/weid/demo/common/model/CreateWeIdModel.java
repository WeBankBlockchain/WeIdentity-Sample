package com.webank.weid.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 公私钥接口模板.
 * @author darwindu
 * @date 2020/1/8
 **/
@ApiModel(description = "公私钥接口模板")
public class CreateWeIdModel {

    @ApiModelProperty(name = "publicKey", value = "公钥", required = true,
        example = "\"10884791889573885961573595923034451846181288826676102791986473114109409913876511177868710733244026350522372286607643048335587344589818301443523106616678574\"")
    private String publicKey;

    @ApiModelProperty(name = "privateKey", value = "私钥", required = true,
        example = "\"53079349606873082534274061523339694826923290877435862289172419522326067705985\"")
    private String privateKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
