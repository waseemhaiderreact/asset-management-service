package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetDTO;
import com.sharklabs.ams.assetGroup.AssetGroupDTO;

import java.util.List;

public class AssetAndAssetGroupResponse {

    private List<AssetGroupDTO> assetGroupDTOS;
    private String responseIdentifier;
    private List<AssetDTO> assetDTOS;

    public List<AssetGroupDTO> getAssetGroupDTOS() {
        return assetGroupDTOS;
    }

    public void setAssetGroupDTOS(List<AssetGroupDTO> assetGroupDTOS) {
        this.assetGroupDTOS = assetGroupDTOS;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public List<AssetDTO> getAssetDTOS() {
        return assetDTOS;
    }

    public void setAssetDTOS(List<AssetDTO> assetDTOS) {
        this.assetDTOS = assetDTOS;
    }


}
