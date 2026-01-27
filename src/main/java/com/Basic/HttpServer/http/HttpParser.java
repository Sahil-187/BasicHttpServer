package com.Basic.HttpServer.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //32
    private static final int CR = 0x0D; //13
    private static final int LF = 0x0A; //10

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest httpRequest = new HttpRequest();
        try {
            parseRequestLine(reader, httpRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (BadHTTPVersionException e) {
            throw new RuntimeException(e);
        }
        parseHeaders(reader, httpRequest);
        parseBody(reader, httpRequest);
        return httpRequest;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest httpRequest) throws IOException, HttpParsingException, BadHTTPVersionException {
        int _byte;
        StringBuffer processingDataBuffer = new StringBuffer();
        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        while((_byte = reader.read()) >= 0) {
            if(_byte == CR) {
                _byte = reader.read();
                if(_byte == LF) {
                    if(!methodParsed || !requestTargetParsed || processingDataBuffer.toString().isEmpty()) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    try {
                        httpRequest.setHttpVersion(processingDataBuffer.toString());
                    } catch(BadHTTPVersionException e) {
                        e.printStackTrace();
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return ;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if(_byte == SP) {
                if(!methodParsed) {
                    httpRequest.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                } else if(!requestTargetParsed) {
                    httpRequest.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char)_byte);
                if (!methodParsed) {
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest httpRequest) {
    }

    private void parseBody(InputStreamReader reader, HttpRequest httpRequest) {
    }

}
