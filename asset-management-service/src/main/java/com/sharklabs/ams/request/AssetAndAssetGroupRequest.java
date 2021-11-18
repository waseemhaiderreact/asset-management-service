package com.sharklabs.ams.request;

import java.util.Set;

public class AssetAndAssetGroupRequest {

    public Set<String> assetUUIDs;
    public Set<String> assetGroupUUIDs;
    String accessKey;

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
