package com.sharklabs.ams.request;

import org.springframework.web.multipart.MultipartFile;

public class ImportBulkAssetRequest {

    private String tenantUUID;

    private String userUUID;

    private String userName;

    private String importType;

    private MultipartFile file;

    public ImportBulkAssetRequest() {
    }

    public ImportBulkAssetRequest(String tenantUUID, String userUUID, String userName, String importType) {
        this.tenantUUID = tenantUUID;
        this.userUUID = userUUID;
        this.userName = userName;
        this.importType = importType;
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

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
