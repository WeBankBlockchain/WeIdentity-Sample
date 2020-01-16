package com.webank.weid.demo.common.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 验证电子凭证接口模板.
 * @author darwindu
 * @date 2020/1/2
 **/
@ApiModel(description = "验证电子凭证接口模板")
public class VerifyCredentialPoJoModel {

    @ApiModelProperty(name = "issuer", value = "WeIdentity DID", required = true,
        example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String issuerWeId;

    @ApiModelProperty(name = "credential", value = "电子凭证", required = true,
        example = "{\n"
            + "    \"context\": \"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\",\n"
            + "    \"id\": \"2cd5aced-a30a-4934-803d-fb0f090957a4\",\n"
            + "    \"cptId\": 1001,\n"
            + "    \"issuer\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\",\n"
            + "    \"issuanceDate\": 1578466962,\n"
            + "    \"expirationDate\": 1578517027,\n"
            + "    \"claim\": {\n"
            + "      \"gender\": \"F\",\n"
            + "      \"name\": \"zhang san\",\n"
            + "      \"age\": 32\n"
            + "    },\n"
            + "    \"proof\": {\n"
            + "      \"creator\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41#key0\",\n"
            + "      \"salt\": {\n"
            + "        \"gender\": \"r2bAe\",\n"
            + "        \"name\": \"m5CrH\",\n"
            + "        \"age\": \"lbRPX\"\n"
            + "      },\n"
            + "      \"created\": 1578466962,\n"
            + "      \"type\": \"Secp256k1\",\n"
            + "      \"signatureValue\": \"G9jxiFBUokDr7RkpOpiYSdIn7mYie/sGXUP+zCyG/4QgMweRBo3Ew8e28ZQLPH242Rl1DcwHhqLQ4tuDBYs2f5I=\"\n"
            + "    },\n"
            + "    \"type\": [\n"
            + "      \"VerifiableCredential\"\n"
            + "    ],\n"
            + "    \"signature\": \"G9jxiFBUokDr7RkpOpiYSdIn7mYie/sGXUP+zCyG/4QgMweRBo3Ew8e28ZQLPH242Rl1DcwHhqLQ4tuDBYs2f5I=\",\n"
            + "    \"salt\": {\n"
            + "      \"gender\": \"r2bAe\",\n"
            + "      \"name\": \"m5CrH\",\n"
            + "      \"age\": \"lbRPX\"\n"
            + "    },\n"
            + "    \"proofType\": \"Secp256k1\",\n"
            + "    \"hash\": \"0x3d059391a6adb36443c148728eba40a9f280f553ce8043b8c88b9afa7495a39b\",\n"
            + "    \"signatureThumbprint\": \"{\\\"claim\\\":\\\"{\\\"age\\\":\\\"0x98cf672409c25cf2f0149ceccd505e9c75a024273c5588bd88e0c8f362c01422\\\",\\\"gender\\\":\\\"0x8eb2b804a8f36f7257dc708f1629cd47b292566a00066cb76157591d45dcef92\\\",\\\"name\\\":\\\"0x0303a5b639ba700bec18eb9d8d9488a8a7d18371f95a14bd49209f3ddde60136\\\"}\\\",\\\"context\\\":\\\"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\\\",\\\"cptId\\\":1001,\\\"expirationDate\\\":1578517027,\\\"id\\\":\\\"2cd5aced-a30a-4934-803d-fb0f090957a4\\\",\\\"issuanceDate\\\":1578466962,\\\"issuer\\\":\\\"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\\\",\\\"proof\\\":null,\\\"type\\\":[\\\"VerifiableCredential\\\"]}\"\n"
            + "}")
    private Map<String, Object> credential;

    public String getIssuerWeId() {
        return issuerWeId;
    }

    public void setIssuerWeId(String issuerWeId) {
        this.issuerWeId = issuerWeId;
    }

    public Map<String, Object> getCredential() {
        return credential;
    }

    public void setCredential(Map<String, Object> credential) {
        this.credential = credential;
    }
}
