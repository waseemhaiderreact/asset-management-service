package com.sharklabs.ams.request;
import java.util.Date;
public class CreateWalletRequest {

    private String walletType;
    private String productType;
    private String capacityUnit;
    private Double capacity;
    private String orgUUID;
    private String assetUUID;
    private String userUUID;
    private String walletName;
    private String thresholdType;
    private Double thresholdValue;
    private Boolean allowedNegative;
    public String getWalletName() {
        return walletName;
    }
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
    public String getThresholdType() {
        return thresholdType;
    }
    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }
    public Double getThresholdValue() {
        return thresholdValue;
    }
    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }
    public Boolean getAllowedNegative() {
        return allowedNegative;
    }
    public void setAllowedNegative(Boolean allowedNegative) {
        this.allowedNegative = allowedNegative;
    }
    public String getCapacityUnit() {
        return capacityUnit;
    }
    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }
    public Double getCapacity() {
        return capacity;
    }
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
    public String getWalletType() {
        return walletType;
    }
    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public String getOrgUUID() {
        return orgUUID;
    }
    public void setOrgUUID(String orgUUID) {
        this.orgUUID = orgUUID;
    }
    public String getAssetUUID() {
        return assetUUID;
    }
    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }
    public String getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }



}
