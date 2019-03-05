package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;
import org.springframework.data.domain.Page;

public class GetPaginatedAssetsResponse {
    private String responseIdentifier;
    private Page<Asset> assets;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public Page<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Page<Asset> assets) {
        this.assets = assets;
    }
}
