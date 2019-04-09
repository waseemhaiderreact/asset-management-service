package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.asset.AssetModel;

import java.util.List;

public class GetAssetsResponse {
    private List<AssetModel> assets;
    private String responseIdentifier;

    public List<AssetModel> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetModel> assets) {
        this.assets = assets;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
