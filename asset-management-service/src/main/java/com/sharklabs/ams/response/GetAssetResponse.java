package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.asset.AssetResponse;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;

public class GetAssetResponse {
    private AssetResponse asset;
    private FieldTemplate fieldTemplate;
    private String responseIdentifier;

    public AssetResponse getAsset() {
        return asset;
    }

    public void setAsset(AssetResponse asset) {
        this.asset = asset;
    }

    public FieldTemplate getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplate fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
