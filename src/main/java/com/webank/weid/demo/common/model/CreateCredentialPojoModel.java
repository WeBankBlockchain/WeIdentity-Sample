package com.webank.weid.demo.common.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 创建电子凭证接口模板.
 * @author darwindu
 * @date 2020/1/6
 **/
@ApiModel(description = "创建电子凭证接口模板")
public class CreateCredentialPojoModel {

    @ApiModelProperty(name = "cptId", value = "CPT编号", required = true,
        example = "1001")
    private Integer cptId;

    @ApiModelProperty(name = "issuer", value = "发行方WeIdentity DID", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String issuer;

    @ApiModelProperty(name = "claimData", value = "claim数据", required = true,
        example = "{\n"
            + "    \n"
            + "    \"age\": 32,\n"
            + "    \"name\": \"zhang san\",\n"
            + "    \"gender\": \"F\"\n"
            + "}")
    private Map<String, Object> claimData;

    public Integer getCptId() {
        return cptId;
    }

    public void setCptId(Integer cptId) {
        this.cptId = cptId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Map<String, Object> getClaimData() {
        return claimData;
    }

    public void setClaimData(Map<String, Object> claimData) {
        this.claimData = claimData;
    }
}
