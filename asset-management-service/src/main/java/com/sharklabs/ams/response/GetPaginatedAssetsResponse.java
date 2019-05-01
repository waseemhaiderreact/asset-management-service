package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetModelForTableView;
import com.sharklabs.ams.page.AssetPage;

public class GetPaginatedAssetsResponse {
    private String responseIdentifier;
    private AssetPage assets;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public AssetPage getAssets() {
        return assets;
    }

    public void setAssets(AssetPage assets) {
        this.assets = assets;
    }
}
