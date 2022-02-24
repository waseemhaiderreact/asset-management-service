package com.sharklabs.ams.asset;

public class AssetInfoDTO {

    private String uuid;

    private String name;

    private String assetNumber;

    private String category;

    private String categoryUUID;

    private String primaryUsageUnit;

    private String secondaryUsageUnit;

    private String consumptionUnit;

    public AssetInfoDTO(String uuid, String name, String assetNumber, String category, String categoryUUID) {
        this.uuid = uuid;
        this.name = name;
        this.assetNumber = assetNumber;
        this.category = category;
        this.categoryUUID = categoryUUID;
    }

    public AssetInfoDTO(String uuid, String name, String assetNumber, String category, String categoryUUID, String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit) {
        this.uuid = uuid;
        this.name = name;
        this.assetNumber = assetNumber;
        this.category = category;
        this.categoryUUID = categoryUUID;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
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

    public String getPrimaryUsageUnit() {
        return primaryUsageUnit;
    }

    public void setPrimaryUsageUnit(String primaryUsageUnit) {
        this.primaryUsageUnit = primaryUsageUnit;
    }

    public String getSecondaryUsageUnit() {
        return secondaryUsageUnit;
    }

    public void setSecondaryUsageUnit(String secondaryUsageUnit) {
        this.secondaryUsageUnit = secondaryUsageUnit;
    }

    public String getConsumptionUnit() {
        return consumptionUnit;
    }

    public void setConsumptionUnit(String consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }
}
