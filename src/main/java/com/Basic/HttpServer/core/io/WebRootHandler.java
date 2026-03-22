package com.Basic.HttpServer.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class WebRootHandler {
    private File webRoot;

    public WebRootHandler(String webRootPath) throws WebRootNotFoundException {
        webRoot = new File(webRootPath);
        if(!webRoot.exists() || !webRoot.isDirectory()) {
            throw new WebRootNotFoundException("Webroot provided does not exist or is not a folder");
        }
    }

    private boolean checkIfEndsWithSlash(String relativePath) {
        return relativePath.endsWith("/");
    }

    private boolean checkIfProvidedRelativePathExists(String relativePath) {
        File file = new File(webRoot, "relativePath");

        if (!file.exists()) {
            return false;
        }
        try {
            if (file.getCanonicalPath().startsWith(webRoot.getCanonicalPath())) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public String getFileMimeType(String relativePath) throws FileNotFoundException {

        if(checkIfEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }

        if(!checkIfProvidedRelativePathExists(relativePath)) {
            throw new FileNotFoundException("File not found: "+ relativePath);
        }

        File file = new File(webRoot, relativePath);
        String mineType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

        if (mineType == null) {
            return "application/octet-stream";
        }

        return mineType;
    }

    public byte[] getFileByteArrayData(String relativePath) throws FileNotFoundException {
        if (checkIfEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }

        if(!checkIfProvidedRelativePathExists(relativePath)) {
            throw new FileNotFoundException("File not found: "+ relativePath);
        }

        File file = new File(webRoot, relativePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int)file.length()];
        try {
            fileInputStream.read(fileBytes);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBytes;
    }
}
