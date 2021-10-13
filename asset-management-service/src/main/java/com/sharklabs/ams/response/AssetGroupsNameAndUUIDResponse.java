package com.sharklabs.ams.response;

import com.sharklabs.ams.minimalinfo.MinimalInfo;

import java.util.List;

public class AssetGroupsNameAndUUIDResponse {

    private List<MinimalInfo.AssetGroupInfo> assetGroupInfos;

    private String responseIdentifer;

    public List<MinimalInfo.AssetGroupInfo> getAssetGroupInfos() {
        return assetGroupInfos;
    }

    public void setAssetGroupInfos(List<MinimalInfo.AssetGroupInfo> assetGroupInfos) {
        this.assetGroupInfos = assetGroupInfos;
    }

    public String getResponseIdentifer() {
        return responseIdentifer;
    }

    public void setResponseIdentifer(String responseIdentifer) {
        this.responseIdentifer = responseIdentifer;
    }
}
