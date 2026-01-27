package com.Basic.HttpServer.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_VERSION("HTTP/1.1",1,1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    HttpVersion(String literal, int major, int minor) {
        LITERAL = literal;
        MAJOR = major;
        MINOR = minor;
    }

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHTTPVersionException {
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);

        if(!matcher.find() || matcher.groupCount() != 2) {
            throw new BadHTTPVersionException();
        }
        HttpVersion bestCompatibleVersion = null;

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        for(HttpVersion version : HttpVersion.values()) {
            if(version.LITERAL.equals(literalVersion)) {
                return version;
            }
            if(version.MAJOR == major) {
                if(version.MINOR <= minor) {
                    bestCompatibleVersion = version;
                }
            }
        }

        return bestCompatibleVersion;
    }
}
