package com.github.mideo.apitestkit;


import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ApiTestKitProperties {
    private final Logger LOGGER = Logger.getLogger("ApiTestKitProperties");
    private Properties config = new Properties();

    private static ApiTestKitProperties INSTANCE;

    public static ApiTestKitProperties create() {
        if (INSTANCE == null){
            INSTANCE = new ApiTestKitProperties("apitestkit.properties");
        }
        return INSTANCE;
    }

    private ApiTestKitProperties(String propertyFileName) {
        load(propertyFileName);
    }


    public void load(String propertyFileName){
        config.clear();
        updateConfig(propertyFileName);
    }

    private void updateConfig(String propertyFileName) {
        try {
            config.load(getClass().getClassLoader().getResourceAsStream(propertyFileName));
        } catch (NullPointerException | IOException e) {
            LOGGER.info("No properties file to load! using defaults");
        }
    }


    public void set(String s, Object o){
        config.setProperty(s, String.valueOf(o));
    }

    public Object get(String name, Object defaultValue) {
        return config.getProperty(name, defaultValue.toString());
    }

    public Object get(String name) {
        return config.getProperty(name, null);
    }

    public int getInt(String name) {
        return Integer.valueOf(get(name).toString());
    }

    public int getInt(String name, int defaultValue) {
        return Integer.valueOf(get(name, defaultValue).toString());
    }

    public String getString(String name) {
        return (String) get(name);
    }

    public String getString(String name, String defaultValue) {
        return (String) get(name,defaultValue);
    }

}
