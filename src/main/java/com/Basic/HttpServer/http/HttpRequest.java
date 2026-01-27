package com.Basic.HttpServer.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion;
    private HttpVersion httpVersion;

    HttpRequest() {}

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public String getOriginalHttpVersion() {
        return this.originalHttpVersion;
    }

    public void setMethod(String methodName) throws HttpParsingException {
        for(HttpMethod method : HttpMethod.values()) {
            if(method.toString().equals(methodName)) {
                this.method = method;
                return ;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public void setHttpVersion(String originalHttpVersion) throws BadHTTPVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.httpVersion = HttpVersion.getBestCompatibleVersion(this.originalHttpVersion);
        if(this.httpVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTPVERSION_NOT_SUPPORTED);
        }
    }
}
