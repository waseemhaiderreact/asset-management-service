package com.sharklabs.ams.request;

import java.util.ArrayList;

public class AddAssetGroupRequest {

    private ArrayList<String> assetUUIDs;
    private String groupName;
    private String createdByUserName;
    private String category;
    private String createdByUserUUID;
    private String tenantUUID;


    public ArrayList<String> getAssetUUIDs() {
        return assetUUIDs;
    }

    public void setAssetUUIDs(ArrayList<String> assetUUIDs) {
        this.assetUUIDs = assetUUIDs;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getCategory() {    return category;  }

    public void setCategory(String category) {    this.category = category;    }

    public String getCreatedByUserUUID() {
        return createdByUserUUID;
    }

    public void setCreatedByUserUUID(String createdByUserUUID) {
        this.createdByUserUUID = createdByUserUUID;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }
}
