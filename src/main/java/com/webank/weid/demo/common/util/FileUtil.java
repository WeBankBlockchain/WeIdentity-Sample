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
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * check the path is exists, create and return the path if it does not exist.
     * @param path the path
     * @return returns the path
     */
    public static String checkDir(String path) {

        String checkPath = path;

        // stitching the last slash.
        if (!checkPath.endsWith(SLASH_CHARACTER)) {
            checkPath = checkPath + SLASH_CHARACTER;
        }
        
        // check the path, create the path when it does not exist.
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

        logger.info("get data form [{}]", path);
        FileInputStream fis = null;
        String str = null;
        try {
            fis = new FileInputStream(path);
            byte[] buff = new byte[fis.available()];
            int size = fis.read(buff);
            if (size > 0) {
                str = new String(buff, StandardCharsets.UTF_8);
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
     * save data in a specified file.
     * 
     * @param filePath save file path
     * @param dataStr save data
     * @return return the file path
     */
    public static String saveFile(String filePath, String dataStr) {

        logger.info("save data in to [{}]", filePath);
        OutputStreamWriter ow = null;
        try {
            File file = new File(filePath);
            ow = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            ow.write(dataStr);
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
