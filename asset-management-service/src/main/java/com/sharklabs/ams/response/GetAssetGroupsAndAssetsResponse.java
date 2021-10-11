package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetNameAndUUIDModel;

import java.util.HashMap;
import java.util.List;

public class GetAssetGroupsAndAssetsResponse {

    private String responseIdentifier;

    private List<AssetNameAndUUIDModel> assets;

    private List<AssetNameAndUUIDModel> assetGroups;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public List<AssetNameAndUUIDModel> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetNameAndUUIDModel> assets) {
        this.assets = assets;
    }

    public List<AssetNameAndUUIDModel> getAssetGroups() {
        return assetGroups;
    }

    public void setAssetGroups(List<AssetNameAndUUIDModel> assetGroups) {
        this.assetGroups = assetGroups;
    }
}
