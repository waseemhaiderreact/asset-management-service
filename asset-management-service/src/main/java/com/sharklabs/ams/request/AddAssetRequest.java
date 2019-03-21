package com.sharklabs.ams.request;

import com.sharklabs.ams.asset.Asset;

import java.util.HashSet;
import java.util.Set;

public class AddAssetRequest {
    private Asset asset;
    private String categoryId;
    private Set<String> images=new HashSet();

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

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }
}
