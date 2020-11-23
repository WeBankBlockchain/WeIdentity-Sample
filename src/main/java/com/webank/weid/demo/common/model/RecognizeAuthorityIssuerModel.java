package com.webank.weid.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 权威机构认证接口模板.
 * @author v_wbgyang
 * @date 2020/11/23
 **/
@ApiModel(description = "权威机构认证接口模板")
public class RecognizeAuthorityIssuerModel {

    @ApiModelProperty(name = "issuer", value = "授权机构weid", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String issuer;

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
    }

}
