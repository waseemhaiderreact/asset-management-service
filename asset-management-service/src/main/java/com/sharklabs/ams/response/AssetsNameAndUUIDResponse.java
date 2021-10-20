package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetDTO;
import com.sharklabs.ams.assetGroup.AssetGroupDTO;
import com.sharklabs.ams.minimalinfo.MinimalInfo;

import java.io.Serializable;
import java.util.List;

public class AssetsNameAndUUIDResponse implements Serializable {

    private List<AssetDTO> assetDTOS;

    private List<AssetGroupDTO> assetGroupDTOS;

    private String category;

    private String responseIdentifier;

    public AssetsNameAndUUIDResponse(List<AssetDTO> assetDTOS, List<AssetGroupDTO> assetGroupDTOS, String category,String responseIdentifier) {
        this.assetDTOS = assetDTOS;
        this.assetGroupDTOS = assetGroupDTOS;
        this.responseIdentifier = responseIdentifier;
        this.category = category;
    }

    public List<AssetDTO> getAssetDTOS() {
        return assetDTOS;
    }

    public void setAssetDTOS(List<AssetDTO> assetDTOS) {
        this.assetDTOS = assetDTOS;
    }

    public List<AssetGroupDTO> getAssetGroupDTOS() {
        return assetGroupDTOS;
    }

    public void setAssetGroupDTOS(List<AssetGroupDTO> assetGroupDTOS) {
        this.assetGroupDTOS = assetGroupDTOS;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
