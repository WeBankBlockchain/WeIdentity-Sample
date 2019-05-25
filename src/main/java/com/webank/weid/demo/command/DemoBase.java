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

package com.webank.weid.demo.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.webank.weid.demo.common.util.FileUtil;

/**
 * Demo base class for initializing spring containers and getting jsonSchema and claimData.
 * 
 * @author v_wbgyang
 *
 */
public abstract class DemoBase {

    private static final Logger logger = LoggerFactory.getLogger(DemoBase.class);

    /**
     * spring context.
     */
    protected static final  ApplicationContext context;

    /**
     * schema.
     */
    protected static final String SCHEMA1;
    
    /**
     * schema.
     */
    protected static final String SCHEMA2;
    
    /**
     * claimData.
     */
    protected static final String CLAIMDATA;

    static {
        
        // initializing spring containers
        context = new ClassPathXmlApplicationContext(new String[] {
            "classpath:SpringApplicationContext-demo.xml"});
        logger.info("initializing spring containers finish...");
 
        //get jsonSchema data.
        SCHEMA1 = FileUtil.getDataByPath("./claim/JsonSchema1.json");
        
        //get jsonSchema data.
        SCHEMA2 = FileUtil.getDataByPath("./claim/JsonSchema2.json");
        
        //get schemaData data.
        CLAIMDATA = FileUtil.getDataByPath("./claim/ClaimData.json");
    }
}
