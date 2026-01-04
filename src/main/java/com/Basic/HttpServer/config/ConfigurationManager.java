package com.Basic.HttpServer.config;

import com.Basic.HttpServer.util.Json;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager configurationManagement;
    private static Configuration configuration;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if(configurationManagement == null) {
            configurationManagement = new ConfigurationManager();
        }
        return configurationManagement;
    }

    /**
     * Used to load a configuration file by the path provided
     */
    public void loadConfigurationFile(String filePath) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch(FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuffer sb = new StringBuffer();

        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        } catch(IOException e) {
            throw new HttpConfigurationException(e);
        }
        JsonNode conf = null;
        try {
            conf = Json.prase(sb.toString());
        } catch(IOException e) {
            throw new HttpConfigurationException("Error parsing the Configuration File", e);
        }
        try {
            configuration = Json.fromJson(conf, Configuration.class);
        } catch(IOException e) {
            throw new HttpConfigurationException("Error parsing the Configuration File Internal",e);
        }
    }

    /**
     * Returns the current laoded configuration
     */
    public Configuration getCurrentConfiguration() {
        if(configuration == null) {
            throw new HttpConfigurationException("No Current Configuration Set.");
        }
        return configuration;
    }
}
