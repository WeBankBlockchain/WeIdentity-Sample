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

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.webank.weid.demo.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * file tool.
 *
 * @author v_wbgyang
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * slash.
     */
    private static final String SLASH_CHARACTER = "/";

    private static final String RESOURCE_DIR = "./conf/";

    private static final String KEY_DIR = "./keys/priv/";

    private static final String BUILD_TOOL_RESOURCE_DIR = "resources/";

    private static final String BUILD_TOOL_ADMIN_KEY = "output/admin/";

    /**
     * check the path is exists, create and return the path if it does not exist.
     *
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
     * @param dataStr  save data
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

    /**
     * close the input stream
     *
     * @param is input stream
     */
    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("io close exception.", e);
            }
        }
    }

    /**
     * close the output stream
     *
     * @param os output stream
     */
    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                logger.error("io close exception.", e);
            }
        }
    }

    /**
     * copy the source file to target file, if the
     *
     * @param srcFile    soruce file
     * @param targetFile target file
     */
    public static void copy(File srcFile, File targetFile) {
        //创建输入输出流
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            logger.error("the file can not found.", e);
        } catch (IOException e) {
            logger.error("copy file exception.", e);
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * Load the configure file from environment path setted by Build Tool
     *
     * @throws BusinessException
     */
    public static void loadConfigFromEnv() throws BusinessException {
        String build_tool_home = System.getenv("BUILD_TOOL_HOME");

        loadConfig(
                build_tool_home + SLASH_CHARACTER + BUILD_TOOL_RESOURCE_DIR + "fisco.properties",
                RESOURCE_DIR,
                "fisco.properties");
        loadConfig(
                build_tool_home + SLASH_CHARACTER + BUILD_TOOL_RESOURCE_DIR + "node.key",
                RESOURCE_DIR,
                "node.key");
        loadConfig(
                build_tool_home + SLASH_CHARACTER + BUILD_TOOL_RESOURCE_DIR + "node.crt",
                RESOURCE_DIR,
                "node.crt");
        loadConfig(
                build_tool_home + SLASH_CHARACTER + BUILD_TOOL_RESOURCE_DIR + "ca.crt",
                RESOURCE_DIR,
                "ca.crt");
        loadConfig(
                build_tool_home + SLASH_CHARACTER + BUILD_TOOL_RESOURCE_DIR + "weidentity.properties",
                RESOURCE_DIR,
                "weidentity.properties");

        loadConfig(
                build_tool_home + SLASH_CHARACTER + BUILD_TOOL_ADMIN_KEY + "ecdsa_key",
                KEY_DIR,
                "ecdsa_key");

    }

    /**
     * copy the configure file form Build tool setup to /resources,
     * if the environment path is unknown, use the default configure in /resources
     * if the file is not exist in /resources, raise false alarms.
     *
     * @param src  The source configure file
     * @param dest the target configure path
     * @param name the name of file
     * @throws BusinessException
     */
    private static void loadConfig(String src, String dest, String name) throws BusinessException {
        File buildToolConfig = new File(src);
        File sampleConfig = new File(dest + name);
        if (buildToolConfig.exists()) {
            logger.info(String.format("Using the config %s from Build Tool", name));
            if (sampleConfig.exists()) {
                sampleConfig.delete();
            }
            FileUtil.copy(buildToolConfig, sampleConfig);
        } else if (sampleConfig.exists()) {
            logger.info(String.format("Using the default config %s in %s", name, dest));
        } else {
            logger.error(String.format("The file named %s is not in %s ", name, dest));
            logger.error("1. If you have not deployed Build Tool " +
                    "(https://github.com/WeBankFinTech/WeIdentity-Build-Tools), " +
                    "please deploy Build Tool to prepare this file.");
            logger.error("2. If you have deployed Build Tool, please restart the bash.");
            logger.error(String.format("3. If you do not want to deploy Build Tool in your machine, " +
                    "please manually prepare it to %s.", dest));
            throw new BusinessException(String.format("The Config of %s is essential", name));
        }
    }

}
