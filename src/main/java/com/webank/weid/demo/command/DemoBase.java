
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
     * load the Configure from Environment variable
     */
    static {
        FileUtil.loadConfigFromEnv();
    }

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
