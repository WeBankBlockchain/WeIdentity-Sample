package com.webank.weid.demo.common.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 注册CPT接口模板.
 * @author darwindu
 * @date 2020/1/2
 **/
@ApiModel(description = "注册CPT接口模板")
public class CptModel {

    @ApiModelProperty(name = "publisher", value = "发布者weid", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String publisher;

    @ApiModelProperty(name = "claim", value = "CPT数据类型定义", required = true,
        example = "{\n"
            + "    \"title\": \"cpt\",\n"
            + "    \"description\": \"this is cpt\",\n"
            + "    \"properties\" : {\n"
            + "        \"name\": {\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the name of certificate owner\"\n"
            + "        },\n"
            + "        \"gender\": {\n"
            + "            \"enum\": [\"F\", \"M\"],\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the gender of certificate owner\"\n"
            + "        },\n"
            + "        \"age\": {\n"
            + "            \"type\": \"number\",\n"
            + "            \"description\": \"the age of certificate owner\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"required\": [\"name\", \"age\"]\n"
            + "}")
    private Map<String, Object> claim;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Map<String, Object> getClaim() {
        return claim;
    }

    public void setClaim(Map<String, Object> claim) {
        this.claim = claim;
    }
}
