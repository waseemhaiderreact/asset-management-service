package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;

import java.util.List;

public class GetAssetsResponse {
    private List<Asset> assets;
    private String responseIdentifier;

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
