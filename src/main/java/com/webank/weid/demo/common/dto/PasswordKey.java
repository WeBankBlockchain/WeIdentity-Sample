
package com.webank.weid.demo.common.dto;

/**
 * entity classes that store public and private keys.
 * 
 * @author v_wbgyang
 *
 */
public class PasswordKey {

    /**
     * private key.
     */
    private String privateKey;

    /**
     * public key.
     */
    private String publicKey;

    /**
     * get private key.
     * @return private key
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * set private key.
     * @param privateKey private key
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * get public key.
     * @return public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * set public key.
     * @param publicKey public key
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    } 
}
