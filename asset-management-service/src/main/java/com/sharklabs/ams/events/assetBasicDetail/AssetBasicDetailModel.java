package com.sharklabs.ams.events.assetBasicDetail;

public class AssetBasicDetailModel {
    private String assetUUID;
    private String assetName;
    private String assetType;
    private String assetNumber;
    private String categoryUUID;
    private String manufacture;

    public AssetBasicDetailModel(String assetUUID, String assetName, String assetType, String assetNumber, String categoryUUID) {
        this.assetUUID = assetUUID;
        this.assetName = assetName;
        this.assetType = assetType;
        this.assetNumber = assetNumber;
        this.categoryUUID = categoryUUID;
    }

    public AssetBasicDetailModel() {
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }
}
