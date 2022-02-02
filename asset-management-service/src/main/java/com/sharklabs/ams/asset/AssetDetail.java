package com.sharklabs.ams.asset;

import com.sharklabs.ams.category.Category;

import java.util.Date;

public class AssetDetail {
    private Long id;

    private String assetNumber;

    private String uuid;

    private String name;

    private String modelNumber;

    private String manufacture;

    private Date purchaseDate;

    private Date expiryDate;

    private String warranty;

    private String description;

    private String tenantUUID;

    private String primaryUsageUnit;

    private String secondaryUsageUnit;

    private String consumptionUnit;

    private int consumptionPoints;

    private String status;

    private String categoryId;

    public AssetDetail(Long id, String assetNumber, String uuid, String name, String modelNumber, String manufacture,
                       Date purchaseDate, Date expiryDate, String warranty, String description, String tenantUUID,
                       String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit, int consumptionPoints,
                       String status, String categoryId) {
        this.id = id;
        this.assetNumber = assetNumber;
        this.uuid = uuid;
        this.name = name;
        this.modelNumber = modelNumber;
        this.manufacture = manufacture;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.warranty = warranty;
        this.description = description;
        this.tenantUUID = tenantUUID;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
        this.consumptionPoints = consumptionPoints;
        this.status = status;
        this.categoryId = categoryId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
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

    public String getStatus() {   return status;   }

    public void setStatus(String status) {   this.status = status;   }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    /*public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }*/
}
