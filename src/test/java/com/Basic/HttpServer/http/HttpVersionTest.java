package com.Basic.HttpServer.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpVersionTest {

    @Test
    void getRestCompatibleVersionExactMatch() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");

            assertNotNull(version);
        } catch (BadHTTPVersionException e) {
            fail();
        }
    }

    @Test
    void getRestCompatibleVersionBadRequest() {
        try {
            HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHTTPVersionException e) {
            assertNotNull(e);
        }
    }

    @Test
    void getBestCompatibleVersionHigherVersion() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");

            assertNotNull(version);
            assertEquals(HttpVersion.HTTP_VERSION, version);
        } catch (BadHTTPVersionException e) {
            fail();
        }
    }

}