package com.sharklabs.ams.request;


import java.io.Serializable;
import java.util.Set;

public class AssetAndAssetGroupRequest implements Serializable {

    public Set<String> assetUUIDs;
    public Set<String> assetGroupUUIDs;
    String accessKey;
    public String userUUIDs;
    public String tenantUUIDs;

    public String getTenantUUIDs() {
        return tenantUUIDs;
    }

    public void setTenantUUIDs(String tenantUUIDs) {
        this.tenantUUIDs = tenantUUIDs;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Set<String> getAssetGroupUUIDs() {
        return assetGroupUUIDs;
    }

    public void setAssetGroupUUIDs(Set<String> assetGroupUUIDs) {
        this.assetGroupUUIDs = assetGroupUUIDs;
    }

    public Set<String> getAssetUUIDs() {
        return assetUUIDs;
    }

    public void setAssetUUIDs(Set<String> assetUUIDs) {
        this.assetUUIDs = assetUUIDs;
    }
}
