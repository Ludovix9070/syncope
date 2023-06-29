package org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities;

import org.apache.syncope.common.lib.types.IdRepoImplementationType;
import org.apache.syncope.common.lib.types.ImplementationEngine;
import org.apache.syncope.core.persistence.api.entity.Implementation;

public class MyImplementation implements Implementation {

    private ImplementationEngine impEngine;
    private String type;
    private static final long serialVersionUID = 5L;
    public static final String TABLE = "Implementation";
    String body;



    @Override
    public ImplementationEngine getEngine() {
        return ImplementationEngine.JAVA;
    }


    @Override
    public void setEngine(ImplementationEngine engine) {

        //this.impEngine = engine;
    }

    @Override
    public String getType() {
        return IdRepoImplementationType.PASSWORD_RULE;
    }

    @Override
    public void setType(String type) {

        //this.type = type;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getKey(){
        return null;
    }

    @Override
    public void setKey(String key) {

    }
}
