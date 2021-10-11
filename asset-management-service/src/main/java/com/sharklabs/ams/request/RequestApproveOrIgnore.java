package com.sharklabs.ams.request;

public class RequestApproveOrIgnore {
    private String responderUUID;
    private String requestUUID;
    private Boolean ignore;
    private Boolean approve;
    public String getResponderUUID() {
        return responderUUID;
    }
    public void setResponderUUID(String responderUUID) {
        this.responderUUID = responderUUID;
    }
    public String getRequestUUID() {
        return requestUUID;
    }
    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }
    public Boolean getIgnore() {
        return ignore;
    }
    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
    public Boolean getApprove() {
        return approve;
    }
    public void setApprove(Boolean approve) {
        this.approve = approve;
    }
}
