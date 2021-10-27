package com.sharklabs.ams.request;

import java.util.List;
import java.util.Set;

public class AssetGroupByAssetUUIDsRequest {

    public Set<String> assetUUIDs;

    public Set<String> getAssetUUIDs() {
        return assetUUIDs;
    }

    public void setAssetUUIDs(Set<String> assetUUIDs) {
        this.assetUUIDs = assetUUIDs;
    }

}
