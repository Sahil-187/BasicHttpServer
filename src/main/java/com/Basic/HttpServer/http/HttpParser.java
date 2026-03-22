package com.Basic.HttpServer.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } catch (IOException | BadHTTPVersionException e) {
            throw new RuntimeException(e);
        }
        try {
            parseHeaders(reader, httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

    private void parseHeaders(InputStreamReader reader, HttpRequest httpRequest) throws IOException, HttpParsingException {
        boolean crlfFound = false;
        StringBuilder processingDataBuffer = new StringBuilder();
        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if(_byte == LF) {
                    if(!crlfFound) {
                        crlfFound = true;
                        processingSingleHeaderField(processingDataBuffer, httpRequest);
                        processingDataBuffer.delete(0,processingDataBuffer.length());
                    } else {
                        //two consecutive crlf found, end of headers section
                        parseBody(reader, httpRequest);
                    }
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            } else {
                crlfFound = false;
                processingDataBuffer.append((char)_byte);
            }
        }

    }

    private void processingSingleHeaderField(StringBuilder processingDataBuffer, HttpRequest request) {
        String rawDataField = processingDataBuffer.toString();
        int index = rawDataField.indexOf(':');

        if (index == -1) {
            return;
        }

        String fieldName = rawDataField.substring(0, index).trim();
        String fieldValue = rawDataField.substring(index + 1).trim();

        request.addHeader(fieldName, fieldValue);
    }

    private void parseBody(InputStreamReader reader, HttpRequest httpRequest) {
    }

}
