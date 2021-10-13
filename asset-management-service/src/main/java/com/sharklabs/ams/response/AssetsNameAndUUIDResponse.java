package com.sharklabs.ams.response;

import com.sharklabs.ams.minimalinfo.MinimalInfo;

import java.util.List;

public class AssetsNameAndUUIDResponse {

    private List<MinimalInfo.AssetInfo> assetInfos;

    private String responseIdentifier;

    public List<MinimalInfo.AssetInfo> getAssetInfos() {
        return assetInfos;
    }

    public void setAssetInfos(List<MinimalInfo.AssetInfo> assetInfos) {
        this.assetInfos = assetInfos;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
