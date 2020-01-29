package com.sharklabs.ams.request;

import com.sharklabs.ams.assetfield.AssetField;

import java.util.HashSet;
import java.util.Set;

public class UpdateAssetFieldsRequest {

    private Set<AssetField> assetFields = new HashSet<>();
    private String assetUUID;

    public Set<AssetField> getAssetFields() {
        return assetFields;
    }

    public void setAssetFields(Set<AssetField> assetFields) {
        this.assetFields = assetFields;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }
}
