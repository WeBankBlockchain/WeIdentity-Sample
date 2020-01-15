package com.webank.weid.demo.common.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 创建PresentationPolicyE接口模板.
 * @author darwindu
 * @date 2020/1/8
 **/
@ApiModel(description = "创建PresentationPolicyE接口模板")
public class CreatePresentationPolicyEModel {

    @ApiModelProperty(name = "presentationPolicyE", value = "PresentationPolicyE", required = true,
        example = "{\n"
            + "    \"extra\": {\n"
            + "        \"extra1\": \"\",\n"
            + "        \"extra2\": \"\"\n"
            + "    },\n"
            + "    \"id\": 1,\n"
            + "    \"version\": 1,\n"
            + "    \"orgId\": \"organizationA\",\n"
            + "    \"policyPublisherWeId\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\",\n"
            + "    \"policy\": {\n"
            + "        \"1001\": {\n"
            + "            \"fieldsToBeDisclosed\": {\n"
            + "                \"name\": 1,\n"
            + "                \"gender\": 0,\n"
            + "                \"age\": 1\n"
            + "            }\n"
            + "        }\n"
            + "    }\n"
            + "}")
    private Map<String, Object> presentationPolicyE;

    public Map<String, Object> getPresentationPolicyE() {
        return presentationPolicyE;
    }

    public void setPresentationPolicyE(Map<String, Object> presentationPolicyE) {
        this.presentationPolicyE = presentationPolicyE;
    }
}
