package com.Basic.HttpServer.http;

import static java.lang.Math.max;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAX_LENGTH;

    static {
        int maxLength = -1;
        for (HttpMethod method : HttpMethod.values()) {
            maxLength = max(maxLength, method.toString().length());
        }
        MAX_LENGTH = maxLength;
    }
}
