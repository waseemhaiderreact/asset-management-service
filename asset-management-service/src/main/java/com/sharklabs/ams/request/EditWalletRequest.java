package com.sharklabs.ams.request;

public class EditWalletRequest {

    private String capacityUnit;
    private Double capacity;
    private String assetUUID;
    private String userUUID;
    private String walletName;
    private String thresholdType;
    private Double thresholdValue;
    private String WalletUUID;
    private Boolean allowedNegative;
    public String getWalletUUID() {
        return WalletUUID;
    }
    public void setWalletUUID(String walletUUID) {
        WalletUUID = walletUUID;
    }
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
