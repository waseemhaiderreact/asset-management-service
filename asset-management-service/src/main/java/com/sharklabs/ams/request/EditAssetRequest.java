package com.sharklabs.ams.request;

import com.sharklabs.ams.asset.Asset;

public class EditAssetRequest {
    private Asset asset;
    private String categoryId;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
