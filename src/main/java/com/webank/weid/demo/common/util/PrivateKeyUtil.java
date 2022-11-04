
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

    private static final Logger logger = LoggerFactory.getLogger(PrivateKeyUtil.class);

    public static final String KEY_DIR = PropertiesUtils.getProperty("weid.keys.dir");
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
                logger.error("weId is null");
                return false;
            }

            // get the third paragraph of weId.
            String fileName = weId.substring(weId.lastIndexOf(":") + 1);

            // check whether the path exists or not, then create the path and return.
            String checkPath = FileUtil.checkDir(path);
            String filePath = checkPath + fileName;

            logger.info("save private key into file, weId={}, filePath={}", weId, filePath);

            // save the private key information as the file name for the third paragraph of weId.
            FileUtil.saveFile(filePath, privateKey);
            return true;
        } catch (Exception e) {
            logger.error("savePrivateKey error", e);
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
            logger.error("weId is null");
            return StringUtils.EMPTY;
        }

        // get the third paragraph of weId.
        String fileName = weId.substring(weId.lastIndexOf(":") + 1);
        
        // check whether the path exists or not, then create the path and return.
        String checkPath = FileUtil.checkDir(path);
        String filePath = checkPath + fileName;

        logger.info("get private key from file, weId={}, filePath={}", weId, filePath);

        // get private key information from a file according to the third paragraph of weId.
        return FileUtil.getDataByPath(filePath);
    }
}
