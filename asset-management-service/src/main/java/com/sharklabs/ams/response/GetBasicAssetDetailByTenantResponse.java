package com.sharklabs.ams.response;

import java.util.List;

public class GetBasicAssetDetailByTenantResponse {
    private String responseIdentifier;
    List<GetNameAndTypeOfAssetResponse> assets;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public List<GetNameAndTypeOfAssetResponse> getAssets() {
        return assets;
    }

    public void setAssets(List<GetNameAndTypeOfAssetResponse> assets) {
        this.assets = assets;
    }
}
