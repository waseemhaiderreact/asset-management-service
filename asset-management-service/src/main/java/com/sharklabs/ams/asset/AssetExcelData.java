package com.sharklabs.ams.asset;

import com.sharklabs.ams.field.FieldDTO;

import java.util.Date;
import java.util.List;

public class AssetExcelData {

    private String name;

    private String category;

    private String modelNumber;

    private String manufacturer;

    private Date purchaseDate;

    private String status;

    private String warrantyUnit;

    private String warranty;

    private String primaryUsageUnit;

    private String secondaryUsageUnit;

    private String consumptionUnit;

    private String description;

    private List<FieldDTO> additionalFields;

    public AssetExcelData() {
    }

    public AssetExcelData(String name, String category, String modelNumber, String manufacturer, Date purchaseDate, String status, String warrantyUnit, String warranty, String primaryUsageUnit, String secondaryUsageUnit, String consumptionUnit, String description, List<FieldDTO> additionalFields) {
        this.name = name;
        this.category = category;
        this.modelNumber = modelNumber;
        this.manufacturer = manufacturer;
        this.purchaseDate = purchaseDate;
        this.status = status;
        this.warrantyUnit = warrantyUnit;
        this.warranty = warranty;
        this.primaryUsageUnit = primaryUsageUnit;
        this.secondaryUsageUnit = secondaryUsageUnit;
        this.consumptionUnit = consumptionUnit;
        this.description = description;
        this.additionalFields = additionalFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarrantyUnit() {
        return warrantyUnit;
    }

    public void setWarrantyUnit(String warrantyUnit) {
        this.warrantyUnit = warrantyUnit;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FieldDTO> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(List<FieldDTO> additionalFields) {
        this.additionalFields = additionalFields;
    }
}
