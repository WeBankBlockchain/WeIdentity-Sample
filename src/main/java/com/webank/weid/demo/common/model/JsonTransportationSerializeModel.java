package com.webank.weid.demo.common.model;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用于序列化对象,要求对象实现JsonSerializer接口模板.
 * @author darwindu
 * @date 2020/1/8
 **/
@ApiModel(description = "用于序列化对象,要求对象实现JsonSerializer接口模板。")
public class JsonTransportationSerializeModel {

    @ApiModelProperty(name = "verifierWeIdList", value = "verifierWeId列表，指定transportation的认证者,用于权限控制", required = true,
        example = "[\"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\"]")
    private List<String> verifierWeIdList;

    @ApiModelProperty(name = "encodeType", value = "协议配置：0", required = true,
        example = "0", allowableValues = "range[0,1]")
    private String encodeType;

    @ApiModelProperty(name = "presentationE", value = "待序列化对象", required = true,
        example = "{\n"
            + "    \"context\": [\n"
            + "      \"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\"\n"
            + "    ],\n"
            + "    \"type\": [\n"
            + "      \"VerifiablePresentation\"\n"
            + "    ],\n"
            + "    \"verifiableCredential\": [\n"
            + "      {\n"
            + "        \"context\": \"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\",\n"
            + "        \"id\": \"2cd5aced-a30a-4934-803d-fb0f090957a4\",\n"
            + "        \"cptId\": 1001,\n"
            + "        \"issuer\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\",\n"
            + "        \"issuanceDate\": 1578466962,\n"
            + "        \"expirationDate\": 1578517027,\n"
            + "        \"claim\": {\n"
            + "          \"age\": 32,\n"
            + "          \"gender\": \"0x8eb2b804a8f36f7257dc708f1629cd47b292566a00066cb76157591d45dcef92\",\n"
            + "          \"name\": \"zhang san\"\n"
            + "        },\n"
            + "        \"proof\": {\n"
            + "          \"created\": 1578466962,\n"
            + "          \"creator\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41#key0\",\n"
            + "          \"salt\": {\n"
            + "            \"age\": \"lbRPX\",\n"
            + "            \"gender\": \"0\",\n"
            + "            \"name\": \"m5CrH\"\n"
            + "          },\n"
            + "          \"signatureValue\": \"G9jxiFBUokDr7RkpOpiYSdIn7mYie/sGXUP+zCyG/4QgMweRBo3Ew8e28ZQLPH242Rl1DcwHhqLQ4tuDBYs2f5I=\",\n"
            + "          \"type\": \"Secp256k1\"\n"
            + "        },\n"
            + "        \"type\": [\n"
            + "          \"VerifiableCredential\"\n"
            + "        ],\n"
            + "        \"signature\": \"G9jxiFBUokDr7RkpOpiYSdIn7mYie/sGXUP+zCyG/4QgMweRBo3Ew8e28ZQLPH242Rl1DcwHhqLQ4tuDBYs2f5I=\",\n"
            + "        \"hash\": \"0x3d059391a6adb36443c148728eba40a9f280f553ce8043b8c88b9afa7495a39b\",\n"
            + "        \"salt\": {\n"
            + "          \"age\": \"lbRPX\",\n"
            + "          \"gender\": \"0\",\n"
            + "          \"name\": \"m5CrH\"\n"
            + "        },\n"
            + "        \"proofType\": \"Secp256k1\",\n"
            + "        \"signatureThumbprint\": \"{\\\"claim\\\":\\\"{\\\"age\\\":\\\"0x98cf672409c25cf2f0149ceccd505e9c75a024273c5588bd88e0c8f362c01422\\\",\\\"gender\\\":\\\"0x8eb2b804a8f36f7257dc708f1629cd47b292566a00066cb76157591d45dcef92\\\",\\\"name\\\":\\\"0x0303a5b639ba700bec18eb9d8d9488a8a7d18371f95a14bd49209f3ddde60136\\\"}\\\",\\\"context\\\":\\\"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\\\",\\\"cptId\\\":1001,\\\"expirationDate\\\":1578517027,\\\"id\\\":\\\"2cd5aced-a30a-4934-803d-fb0f090957a4\\\",\\\"issuanceDate\\\":1578466962,\\\"issuer\\\":\\\"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\\\",\\\"proof\\\":null,\\\"type\\\":[\\\"VerifiableCredential\\\"]}\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"proof\": {\n"
            + "      \"created\": 1578471192,\n"
            + "      \"type\": \"Secp256k1\",\n"
            + "      \"verificationMethod\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41#key0\",\n"
            + "      \"nonce\": \"jz1nRc4W9GJhXZIAgyBU\",\n"
            + "      \"signatureValue\": \"HHIGj6mJZT/a+2h6G7eBgJb9lEBPEskR9+dv+iV0fN8wShJrb5JorRQlBhMxtCyjfJDXMczsmIUsboq7Rd69otY=\"\n"
            + "    },\n"
            + "    \"signature\": \"HHIGj6mJZT/a+2h6G7eBgJb9lEBPEskR9+dv+iV0fN8wShJrb5JorRQlBhMxtCyjfJDXMczsmIUsboq7Rd69otY=\",\n"
            + "    \"verificationMethod\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41#key0\",\n"
            + "    \"nonce\": \"jz1nRc4W9GJhXZIAgyBU\"\n"
            + "}")
    private Map<String, Object> presentationE;

    public List<String> getVerifierWeIdList() {
        return verifierWeIdList;
    }

    public void setVerifierWeIdList(List<String> verifierWeIdList) {
        this.verifierWeIdList = verifierWeIdList;
    }

    public String getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public Map<String, Object> getPresentationE() {
        return presentationE;
    }

    public void setPresentationE(Map<String, Object> presentationE) {
        this.presentationE = presentationE;
    }
}
