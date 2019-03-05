package com.sharklabs.ams.response;

import java.util.HashMap;

public class GetNameAndTypeOfAssetsByUUIDSResponse {
    private String responseIdentifier;
    private HashMap<String, GetNameAndTypeOfAssetResponse> assets=new HashMap<>();

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public HashMap<String, GetNameAndTypeOfAssetResponse> getAssets() {
        return assets;
    }

    public void setAssets(HashMap<String, GetNameAndTypeOfAssetResponse> assets) {
        this.assets = assets;
    }
}
