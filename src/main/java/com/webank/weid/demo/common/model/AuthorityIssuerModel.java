package com.webank.weid.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 注册权威机构接口模板.
 * @author darwindu
 * @date 2020/1/2
 **/
@ApiModel(description = "注册权威机构接口模板")
public class AuthorityIssuerModel {

    @ApiModelProperty(name = "issuer", value = "授权机构weid", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String issuer;

    @ApiModelProperty(name = "orgId",
        value = "授权机构名称，机构名称必须小于32个字节，非空，且仅包含ASCII码可打印字符（ASCII值位于32~126）",
        required = true, example = "wb")
    private String orgId;

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getOrgId() {
        return orgId;
    }
}
