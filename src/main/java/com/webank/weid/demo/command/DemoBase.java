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

import com.webank.weid.demo.common.util.FileUtil;

/**
 * Demo base class for initializing spring containers and getting jsonSchema and claimData.
 * 
 * @author v_wbgyang
 *
 */
public abstract class DemoBase {

    /**
     * schema.
     */
    protected static final String SCHEMA1;
    
    /**
     * schema.
     */
    protected static final String SCHEMA2;
    
    /**
     * schema.
     */
    protected static final String SCHEMA3;
    
    /**
     * claimData.
     */
    protected static final String CLAIMDATA;

    /**
     * the demo service.
     */
    protected static DemoService demoService = new DemoService();
    
    static {
        
        //get jsonSchema data.
        SCHEMA1 = FileUtil.getDataByPath("./claim/JsonSchema1.json");
        
        //get jsonSchema data.
        SCHEMA2 = FileUtil.getDataByPath("./claim/JsonSchema2.json");
        
        //get jsonSchema data.
        SCHEMA3 = FileUtil.getDataByPath("./claim/JsonSchema3.json");
        
        //get schemaData data.
        CLAIMDATA = FileUtil.getDataByPath("./claim/ClaimData.json");
    }
}
