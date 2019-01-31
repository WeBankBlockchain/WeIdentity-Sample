/*
 *       Copyright© (2019) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-sample.
 *
 *       weidentity-sample is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-sample is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-sample.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.webank.weid.demo.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.constant.WeIdConstant;

/**
 * file tool.
 * 
 * @author v_wbgyang
 *
 */
public class FileUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    
    /**
     * slash.
     */
    private static final String SLASH_CHARACTER = "/";
    
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
            String checkPath = checkDir(path);
            String filePath = checkPath + fileName;
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
            return StringUtils.EMPTY;
        }
        String fileName = weId.substring(weId.lastIndexOf(":") + 1);
        String checkPath = checkDir(path);
        String filePath = checkPath + fileName;
        return getDataByPath(filePath);
    }
    
    /**
     * check the path is exists, create and return the path if it does not exist.
     * @param path the path
     * @return returns the path
     */
    public static String checkDir(String path) {
        
        String checkPath = path;
        if (!checkPath.endsWith(SLASH_CHARACTER)) {
            checkPath = checkPath + SLASH_CHARACTER;
        }
        File checkDir = new File(checkPath);
        if (!checkDir.exists()) {
            boolean success = checkDir.mkdirs();
            if (!success) {
                logger.error("checkDir.mkdirs");
            }
        }
        return checkPath;
    }

    /**
     * read data from the path.
     * 
     * @param path the path
     * @return returns the data
     */
    public static String getDataByPath(String path) {
        
        FileInputStream fis = null;
        String str = null;
        try {
            fis = new FileInputStream(path);
            byte[] buff = new byte[fis.available()];
            int size = fis.read(buff);
            if (size > 0) {
                str = new String(buff, WeIdConstant.UTF_8);
            }
        } catch (FileNotFoundException e) {
            logger.error("getDataByPath error", e);
        } catch (IOException e) {
            logger.error("getDataByPath error", e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("getDataByPath error", e);
                }
            }
        }
        return str;
    }
    
    /**
     * format Object to String.
     * @return
     */
    public static String formatObjectToString(Object obj) {
        
        ObjectMapper mapper = new ObjectMapper();
        String dataStr = "";
        try {
            dataStr = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("writeValueAsString error:", e);
        }
        return dataStr;
    }
    
    /**
     * save data.
     * @param filePath save file path
     * @param dataStr save data
     * @return
     */
    public static String saveFile(String filePath, String dataStr) {
        
        OutputStreamWriter ow = null;
        try {
            String fileStr = filePath;
            File file = new File(fileStr);
            ow = new OutputStreamWriter(new FileOutputStream(file), WeIdConstant.UTF_8);
            String content = new StringBuffer().append(dataStr).toString();
            ow.write(content);
            return file.getAbsolutePath();
        } catch (IOException e) {
            logger.error("writer file exception", e);
        } finally {
            if (null != ow) {
                try {
                    ow.close();
                } catch (IOException e) {
                    logger.error("io close exception", e);
                }
            }
        }
        return StringUtils.EMPTY;
    }
}
