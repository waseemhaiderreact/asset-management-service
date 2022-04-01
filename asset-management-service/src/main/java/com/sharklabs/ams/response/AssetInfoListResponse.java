package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetInfoDTO;

import java.io.Serializable;
import java.util.List;

public class AssetInfoListResponse implements Serializable {

    private List<AssetInfoDTO> assetDTOS;

    private String responseIdentifier;

    public List<AssetInfoDTO> getAssetDTOS() {
        return assetDTOS;
    }

    public void setAssetDTOS(List<AssetInfoDTO> assetDTOS) {
        this.assetDTOS = assetDTOS;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
