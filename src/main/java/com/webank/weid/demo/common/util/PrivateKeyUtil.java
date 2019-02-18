package com.webank.weid.demo.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the util of the private key.
 * @author v_wbgyang
 *
 */
public class PrivateKeyUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivateKeyUtil.class);
    
    /**
     * SDK private key storage path.
     */
    public static final String SDK_PRIVKEY_PATH = 
        PropertiesUtils.getProperty("admin.privKeyPath");
    
    /**
     * this method stores weId private key information by file and stores
     * private key information by itself in actual scene.
     * 
     * @param path save path
     * @param weId the weId
     * @param privateKey the private key
     * @return returns saved results
     */
    public static boolean savePrivateKey(String path, String weId, String privateKey) {

        try {
            if (null == weId) {
                return false;
            }
            String fileName = weId.substring(weId.lastIndexOf(":") + 1);
            String checkPath = FileUtil.checkDir(path);
            String filePath = checkPath + fileName;
            FileUtil.saveFile(filePath, privateKey);
            return true;
        } catch (Exception e) {
            LOGGER.error("savePrivateKey error", e);
        } 
        return false;    
    }
    
    /**
     * get the private key by weId.
     * 
     * @param path the path
     * @param weId the weId
     * @return returns the private key
     */
    public static String getPrivateKeyByWeId(String path, String weId) {
        
        if (null == weId) {
            return StringUtils.EMPTY;
        }
        String fileName = weId.substring(weId.lastIndexOf(":") + 1);
        String checkPath = FileUtil.checkDir(path);
        String filePath = checkPath + fileName;
        return FileUtil.getDataByPath(filePath);
    }
}
