package com.sharklabs.ams.response;

import com.sharklabs.ams.assetGroup.AssetGroupDTO;

import java.io.Serializable;
import java.util.List;

public class AssetGroupByAssetResponse implements Serializable {

    private List<AssetGroupDTO> assetGroupDTOS;
    private String responseIdentifier;

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



}
