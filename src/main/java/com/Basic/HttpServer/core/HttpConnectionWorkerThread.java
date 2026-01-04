package com.Basic.HttpServer.core;

import com.Basic.HttpServer.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private final Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            LOGGER.info(" * Connection accepted " + socket.getInetAddress());
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>Simple HTTP Server</title></head><body><h1>This page was served using my simple java http server</h1></body</html>";
            final String CLRF = "\n\r"; //13,10
            String response =
                    "HTTP/1.1 200 OK"+CLRF+ //static Line : HTTP VERSION RESPONSE_CODE RESPONSE_CODE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CLRF + //Header
                            CLRF +
                            html + // Body
                            CLRF + CLRF;

            outputStream.write(response.getBytes());

            LOGGER.info("Connection Processing Finished");
        } catch(IOException e) {
            LOGGER.error("Problem with connection",e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Problem in closing stream or socket", e);
            }
        }
    }
}
