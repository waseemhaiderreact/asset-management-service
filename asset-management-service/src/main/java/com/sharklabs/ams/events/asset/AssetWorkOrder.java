package com.sharklabs.ams.events.asset;

public class AssetWorkOrder {

    private String previousStatus;

    private String updatedStatus;

    private String action;

    private String assetUUID;

    private String cost;

    private String beforeCost;

    public AssetWorkOrder() {
    }

    public AssetWorkOrder(String previousStatus, String updatedStatus, String action, String assetUUID) {
        this.previousStatus = previousStatus;
        this.updatedStatus = updatedStatus;
        this.action = action;
        this.assetUUID = assetUUID;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getUpdatedStatus() {
        return updatedStatus;
    }

    public void setUpdatedStatus(String updatedStatus) {
        this.updatedStatus = updatedStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBeforeCost() {
        return beforeCost;
    }

    public void setBeforeCost(String beforeCost) {
        this.beforeCost = beforeCost;
    }
}
