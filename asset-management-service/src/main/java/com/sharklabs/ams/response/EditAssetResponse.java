package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;

public class EditAssetResponse {
    private Asset asset;
    private String responseIdentifier;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
