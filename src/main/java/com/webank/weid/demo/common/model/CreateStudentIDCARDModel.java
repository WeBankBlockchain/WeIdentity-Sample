package com.webank.weid.demo.common.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 创建电子凭证接口.
 * @author zouheliang2011
 * @date 2023/05/12
 **/
@ApiModel(description = "创建电子凭证接口")
public class CreateStudentIDCARDModel {

    @ApiModelProperty(name = "cptId", value = "CPT编号", required = true,
        example = "1005")
    private Integer cptId;

    @ApiModelProperty(name = "issuer", value = "发行方WeIdentity DID", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String issuer;

    @ApiModelProperty(name = "studentIDCARDInfo", value = "cpt模板对应数据", required = true,
        example = "{\n"
            + "    \n"
            + "    \"cardNumber\": \"20230960111201\",\n"
            + "    \"name\": \"zhang san\",\n"
            + "    \"schoolName\": \"中山大学\",\n"
            + "    \"major\": \"english\",\n"
            + "    \"educationalSystem\": \"4 years\",\n"
            + "    \"startTime\": \"2011-09-01\",\n"
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
