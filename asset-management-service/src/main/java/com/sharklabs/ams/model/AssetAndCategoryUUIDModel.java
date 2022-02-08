package com.sharklabs.ams.model;

public class AssetAndCategoryUUIDModel {
    private String assetUUID;
    private String assetName;
    private String categoryName;
    private String categoryUUID;

    public AssetAndCategoryUUIDModel(String assetUUID, String assetName, String categoryName, String categoryUUID) {
        this.assetUUID = assetUUID;
        this.assetName = assetName;
        this.categoryName = categoryName;
        this.categoryUUID = categoryUUID;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }
}
