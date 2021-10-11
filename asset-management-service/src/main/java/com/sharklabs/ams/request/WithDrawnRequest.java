package com.sharklabs.ams.request;

public class WithDrawnRequest {
    private String senderUUID;
    private String requestUUID;
    public String getSenderUUID() {
        return senderUUID;
    }
    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }
    public String getRequestUUID() {
        return requestUUID;
    }
    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }
}
