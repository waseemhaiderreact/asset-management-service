package com.sharklabs.ams.asset;

public class AssetInfoDTO {

    private String uuid;

    private String name;

    private String assetNumber;

    private String category;

    private String categoryUUID;

    public AssetInfoDTO(String uuid, String name, String assetNumber, String category, String categoryUUID) {
        this.uuid = uuid;
        this.name = name;
        this.assetNumber = assetNumber;
        this.category = category;
        this.categoryUUID = categoryUUID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }
}
