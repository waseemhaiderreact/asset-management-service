package com.sharklabs.ams.response;

public class GetFileResponse {
    private byte[] content;
    private String fileName;
    private int contentLength;
    private String responseIdentifier;

    public byte[] getContent() {    return content;
    }

    public void setContent(byte[] content) {     this.content = content;
    }

    public String getFileName() {     return fileName;
    }

    public void setFileName(String fileName) {    this.fileName = fileName;
    }

    public int getContentLength() {    return contentLength;
    }

    public void setContentLength(int contentLength) {    this.contentLength = contentLength;
    }

    public String getResponseIdentifier() {        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {        this.responseIdentifier = responseIdentifier;
    }

}
