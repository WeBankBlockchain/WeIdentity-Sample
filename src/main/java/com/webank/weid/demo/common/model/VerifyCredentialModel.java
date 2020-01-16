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
public class VerifyCredentialModel {

    @ApiModelProperty(name = "credential", value = "电子凭证", required = true,
        example = "{\n"
            + "    \"context\": \"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\",\n"
            + "    \"id\": \"c19ff9f9-4b23-429a-bc09-130209180df5\",\n"
            + "    \"cptId\": 1001,\n"
            + "    \"issuer\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\",\n"
            + "    \"issuanceDate\": 1578467662,\n"
            + "    \"expirationDate\": 4732067662,\n"
            + "    \"claim\": {\n"
            + "      \"gender\": \"F\",\n"
            + "      \"name\": \"zhang san\",\n"
            + "      \"age\": 32\n"
            + "    },\n"
            + "    \"proof\": {\n"
            + "      \"creator\": \"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\",\n"
            + "      \"signature\": \"G1r9auOBUNK6qa/vnWsSdpBg5UW4bXc2nAnbRTRI/kxFHv8w4S5VYUx6cyQ3YxEnErbWMhsvOfA83kiQ/bH5A8A=\",\n"
            + "      \"created\": \"1578467662\",\n"
            + "      \"type\": \"Secp256k1\"\n"
            + "    },\n"
            + "    \"signature\": \"G1r9auOBUNK6qa/vnWsSdpBg5UW4bXc2nAnbRTRI/kxFHv8w4S5VYUx6cyQ3YxEnErbWMhsvOfA83kiQ/bH5A8A=\",\n"
            + "    \"hash\": \"0x804c18e44b71e18339a8481d83fb3cbf89ac27e7a883025b95a4635385f8680e\",\n"
            + "    \"proofType\": \"Secp256k1\",\n"
            + "    \"signatureThumbprint\": \"{\\\"claim\\\":\\\"age0x8b953cbb84328003779eb1ef176ef07f7dd0ae3d4a8e408de53d15a36466c86egender0xe61d9a3d3848fb2cdd9a2ab61e2f21a10ea431275aed628a0557f9dee697c37aname0xd437888f8f49572399b4a94fe4ca3adc1404e4bc0e4e0de11bcdc525071279c7\\\",\\\"context\\\":\\\"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\\\",\\\"cptId\\\":1001,\\\"expirationDate\\\":4732067662,\\\"id\\\":\\\"c19ff9f9-4b23-429a-bc09-130209180df5\\\",\\\"issuanceDate\\\":1578467662,\\\"issuer\\\":\\\"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\\\",\\\"proof\\\":{\\\"created\\\":\\\"1578467662\\\",\\\"creator\\\":\\\"did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41\\\",\\\"signature\\\":\\\"G1r9auOBUNK6qa/vnWsSdpBg5UW4bXc2nAnbRTRI/kxFHv8w4S5VYUx6cyQ3YxEnErbWMhsvOfA83kiQ/bH5A8A=\\\",\\\"type\\\":\\\"Secp256k1\\\"}}\"\n"
            + "}")
    private Map<String, Object> credential;

    public Map<String, Object> getCredential() {
        return credential;
    }

    public void setCredential(Map<String, Object> credential) {
        this.credential = credential;
    }
}
