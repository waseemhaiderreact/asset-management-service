package com.sharklabs.ams.events.asset;

import java.util.Map;

public class AssetModel {
    private String action;
    private Map<String,String> assetUuid;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getAssetUuid() {
        return assetUuid;
    }

    public void setAssetUuid(Map<String, String> assetUuid) {
        this.assetUuid = assetUuid;
    }
}
