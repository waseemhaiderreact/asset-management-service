package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.asset.AssetModel;
import com.sharklabs.ams.asset.AssetNameAndUUIDModel;

import java.util.List;

public class GetAssetsResponse {
    private List<Object> assets;
    private String responseIdentifier;

    public List<Object> getAssets() {
        return assets;
    }

    public void setAssets(List<Object> assets) {
        this.assets = assets;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
