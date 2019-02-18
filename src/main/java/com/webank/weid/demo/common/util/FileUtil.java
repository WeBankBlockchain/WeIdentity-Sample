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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    
    /**
     * slash.
     */
    private static final String SLASH_CHARACTER = "/";
    
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
                LOGGER.error("checkDir.mkdirs");
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
            LOGGER.error("getDataByPath error", e);
        } catch (IOException e) {
            LOGGER.error("getDataByPath error", e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("getDataByPath error", e);
                }
            }
        }
        return str;
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
            LOGGER.error("writer file exception", e);
        } finally {
            if (null != ow) {
                try {
                    ow.close();
                } catch (IOException e) {
                    LOGGER.error("io close exception", e);
                }
            }
        }
        return StringUtils.EMPTY;
    }
}
