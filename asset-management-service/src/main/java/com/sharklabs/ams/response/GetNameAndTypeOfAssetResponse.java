package com.sharklabs.ams.response;

import com.sharklabs.ams.usage.Usage;

public class GetNameAndTypeOfAssetResponse {
    private String name;
    private String type;
    private String categoryUUID;
    private String assetNumber;
    private String uuid;
    private String imageUrl;
    private String primaryUsageUnit;
    private String secondaryUsageUnit;
    private String consumptionUnit;
    private String modelNumber;
    private int consumptionPoints;
    private Usage lastUsage;
    private String manufacture;


    public GetNameAndTypeOfAssetResponse(String name, String type, String assetNumber,String uuid) {
        this.name = name;
        this.type = type;
        this.assetNumber = assetNumber;
        this.uuid=uuid;
    }

    public GetNameAndTypeOfAssetResponse(String name, String categoryUUID, String assetNumber, String uuid, String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit, int consumptionPoints) {
        this.name = name;
        this.categoryUUID = categoryUUID;
        this.assetNumber = assetNumber;
        this.uuid = uuid;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
        this.consumptionPoints = consumptionPoints;
    }

    public GetNameAndTypeOfAssetResponse(String name, String type, String assetNumber, String uuid, String imageUrl, String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit, int consumptionPoints) {
        this.name = name;
        this.type = type;
        this.assetNumber = assetNumber;
        this.uuid = uuid;
        this.imageUrl = imageUrl;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
        this.consumptionPoints = consumptionPoints;
    }

    public GetNameAndTypeOfAssetResponse(String name, String categoryUUID, String assetNumber, String uuid, String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit, int consumptionPoints,String modelNumber) {
        this.name = name;
        this.categoryUUID = categoryUUID;
        this.assetNumber = assetNumber;
        this.uuid = uuid;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
        this.consumptionPoints = consumptionPoints;
        this.modelNumber = modelNumber;
    }

    public GetNameAndTypeOfAssetResponse(String name, String categoryUUID, String assetNumber, String uuid, String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit, int consumptionPoints,String modelNumber, String categoryName) {
        this.name = name;
        this.categoryUUID = categoryUUID;
        this.assetNumber = assetNumber;
        this.uuid = uuid;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
        this.consumptionPoints = consumptionPoints;
        this.modelNumber = modelNumber;
        this.type = categoryName;
    }

    public GetNameAndTypeOfAssetResponse() {
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getConsumptionPoints() {
        return consumptionPoints;
    }

    public void setConsumptionPoints(int consumptionPoints) {
        this.consumptionPoints = consumptionPoints;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    public Usage getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Usage lastUsage) {
        this.lastUsage = lastUsage;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }
}
