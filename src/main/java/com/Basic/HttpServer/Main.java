package com.Basic.HttpServer;

import com.Basic.HttpServer.config.Configuration;
import com.Basic.HttpServer.config.ConfigurationManager;
import com.Basic.HttpServer.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        LOGGER.info("Server starting...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using Port: "+ configuration.getPort());
        LOGGER.info("Using WebRoot: "+ configuration.getWebroot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(configuration.getPort(), configuration.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}