package com.webank.weid.demo.common.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 注册学生模板CPT接口模板.
 * @author zouheliang2011
 * @date 2023/05/12
 **/
@ApiModel(description = "注册学生模板CPT接口模板")
public class StudentIDCARDModel {

    @ApiModelProperty(name = "publisher", value = "发布者weid", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String publisher;

    @ApiModelProperty(name = "credentialInfo", value = "学生证模板CPT数据类型定义", required = true,
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
            + "        \"cardNumber\": {\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the student id card number \"\n"
            + "        },\n"
            + "          \"major\": {\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the major of the student \"\n"
            + "        },\n"
            + "          \"educationalSystem\": {\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the educational system of the student \"\n"
            + "        },\n"
            + "          \"startTime\": {\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the startTime of the student \"\n"
            + "        },\n"
            + "          \"schoolName\": {\n"
            + "            \"type\": \"string\",\n"
            + "            \"description\": \"the school of the student \"\n"
            + "        }\n"
            + "    },\n"
            + "    \"required\": [\"name\", \"gender\", \"cardNumber\", \"major\", \"educationalSystem\", \"startTime\",\"schoolName\"]\n"
            + "}")
    private Map<String, Object> credentialInfo;

    public Map<String, Object> getCredentialInfo() {
        return credentialInfo;
    }

    public void setCredentialInfo(Map<String, Object> credentialInfo) {
        this.credentialInfo = credentialInfo;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
