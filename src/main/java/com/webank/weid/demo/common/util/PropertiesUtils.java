
package com.webank.weid.demo.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.util.PropertyUtils;

/**
 * read Profile tool.
 * 
 * @author v_wbgyang
 *
 */
public class PropertiesUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * Properties object.
     */
    private static Properties props;

    /**
     * configuration file.
     */
    private static final String APPLICATION_FILE = "application.properties";

    static {
        loadProps();
    }

    /**
     * load configuration file.
     */
    private static synchronized void loadProps() {
        props = new Properties();

        InputStream resourceAsStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(APPLICATION_FILE);
        try {
            props.load(resourceAsStream);
            logger.info("loadProps finish...");
        } catch (IOException e) {
            logger.error("loadProps error", e);
        }
        /*
        try{
            InputStream resourceAsStream = new FileInputStream("E:\\weid-afee\\WeIdentity-Sample\\resources\\application.properties");
            props.load(resourceAsStream);
            logger.info("loadProps finish...");
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
         */
    }

    /**
     * read the value in the configuration file according to key.
     * 
     * @param key configured key
     * @return returns the value of key
     */
    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    /**
     * read the value in the configuration file according to key,
     * returns the default value when it is not available.
     * 
     * @param key configured key
     * @param defaultValue  default value
     * @return returns the value of key
     */
    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    /**
     * get the encrypt type.
     * @return encryptType
     */
    public static String getEncryptType() {
       return getProperty("encrypt.type");
    }

    /**
     * get the FISCOBCOS version.
     * @return fiscoVersion
     */
    public static String getFiscoVersion() {
        return getProperty("bcos.version");
    }
}
