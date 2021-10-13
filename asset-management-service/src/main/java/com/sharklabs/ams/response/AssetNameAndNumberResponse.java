package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetInfoDTO;

public class AssetNameAndNumberResponse {

    private AssetInfoDTO assetInfoDTO;

    private String responseIdentifier;

    public AssetInfoDTO getAssetInfoDTO() {
        return assetInfoDTO;
    }

    public void setAssetInfoDTO(AssetInfoDTO assetInfoDTO) {
        this.assetInfoDTO = assetInfoDTO;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
