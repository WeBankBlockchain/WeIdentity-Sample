/*
 *       Copyright© (2018) WeBank Co., Ltd.
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

package com.webank.demo.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.demo.common.util.FileUtil;
import com.webank.weid.constant.WeIdConstant;
import com.webank.weid.protocol.base.Credential;

/**
 * the DemoUtil for command.
 *
 */
public class DemoUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoUtil.class);
    
    private static final String TEMP_DIR = "./tmp/";
    
    private static final String CRED_FILE = TEMP_DIR + "credential.json";
    
    public static final String TEMP_FILE = TEMP_DIR + "temp.data";
    
    /**
     * read credential.
     * @return
     */
    public static Credential getCredentialFromJson() {
        
        Credential credential = null;
        String jsonStr = FileUtil.getDataByPath(CRED_FILE);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            credential = objectMapper.readValue(jsonStr, Credential.class);
        } catch (IOException e) {
            logger.error("getCredentialFromJson error", e);
        }
        return credential;
    }

    /**
     * save credential.
     * @param credential require
     */
    public static String saveCredential(Credential credential) {
        
        ObjectMapper mapper = new ObjectMapper();
        String credentialJson = null;
        try {
            credentialJson = mapper.writeValueAsString(credential);
        } catch (JsonProcessingException e) {
            logger.error("writeValueAsString error:", e);
        }
        
        OutputStreamWriter ow = null;
        try {
            FileUtil.checkDir(TEMP_DIR);
            String fileStr = CRED_FILE;
            File file = new File(fileStr);
            if (file.exists()) {
                if (!file.delete()) {
                    logger.error("delete file fail..");
                }
            }
            ow = new OutputStreamWriter(new FileOutputStream(file), WeIdConstant.UTF_8);
            String content = new StringBuffer().append(credentialJson).toString();
            ow.write(content);
            ow.close();
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
     * save temporary data.
     * @param map require
     */
    public static void saveTemData(Map<String, String> map) {
        ObjectMapper mapper = new ObjectMapper();
        String s = "";
        try {
            s = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        BufferedWriter bufferedWriter = null;
        try {
            FileUtil.checkDir(TEMP_DIR);
            bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(TEMP_FILE), WeIdConstant.UTF_8));
            bufferedWriter.write(s);
        } catch (FileNotFoundException e) {
            logger.error("saveTemData error:", e);
        } catch (IOException e) {
            logger.error("saveTemData error:", e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }    
}
