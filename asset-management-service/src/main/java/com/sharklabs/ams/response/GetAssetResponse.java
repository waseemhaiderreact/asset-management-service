package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.asset.AssetResponse;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;
import com.sharklabs.ams.fieldtemplate.FieldTemplateResponse;

public class GetAssetResponse {
    private AssetResponse asset;
    private FieldTemplateResponse fieldTemplate;
    private String categoryId;
    private String responseIdentifier;

    public AssetResponse getAsset() {
        return asset;
    }

    public void setAsset(AssetResponse asset) {
        this.asset = asset;
    }

    public FieldTemplateResponse getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplateResponse fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
