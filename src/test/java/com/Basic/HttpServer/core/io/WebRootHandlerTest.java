package com.Basic.HttpServer.core.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebRootHandlerTest {
    WebRootHandler webRootHandler;

    private Method checkIfEndsWithSlashMethod;

    @BeforeAll
    public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException {
        webRootHandler = new WebRootHandler("WebRoot");
        Class<WebRootHandler> cls = WebRootHandler.class;
        checkIfEndsWithSlashMethod = cls.getDeclaredMethod("checkIfEndsWithSlash", String.class);
        checkIfEndsWithSlashMethod.setAccessible(true);
    }

    @Test
    void constructorGoodPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("/Users/sahil/Desktop/data/Learning/DEV/JAVA_BE/Projects/BasicHttpServer/WebRoot");
        } catch (WebRootNotFoundException e) {
            fail();
        }
    }

    @Test
    void constructorBadPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("/Users/sahil/Desktop/data/Learning/DEV/JAVA_BE/Projects/BasicHttpServer/WebRoot2");
            fail();
        } catch (WebRootNotFoundException e) {

        }
    }

    @Test
    void constructorRelativeGoodPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot");
        } catch (WebRootNotFoundException e) {
            fail();
        }
    }

    @Test
    void constructorRelativeBadPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot2");
            fail();
        } catch (WebRootNotFoundException e) {
        }
    }

    @Test
    void setCheckIfEndsWithSlashGood() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "index.html");
            assertFalse(result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail(e);
        }
    }

    @Test
    void setCheckIfEndsWithSlashGood2() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "index.html/");
            assertTrue(result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail(e);
        }
    }

    @Test
    void setCheckIfEndsWithSlashGood3() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/private/");
            assertTrue(result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail(e);
        }
    }

    @Test
    void setCheckIfEndsWithSlashGood4() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/private/index.html");
            assertFalse(result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail(e);
        }
    }
}