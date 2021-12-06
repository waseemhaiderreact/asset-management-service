package com.sharklabs.ams.request;

public class ImportBulkAssetRequest {

    private String tenantUUID;

    private String userUUID;

    private String userName;

    public ImportBulkAssetRequest() {
    }

    public ImportBulkAssetRequest(String tenantUUID, String userUUID, String userName) {
        this.tenantUUID = tenantUUID;
        this.userUUID = userUUID;
        this.userName = userName;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
