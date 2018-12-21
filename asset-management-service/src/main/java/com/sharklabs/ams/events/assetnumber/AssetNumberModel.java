package com.sharklabs.ams.events.assetnumber;

public class AssetNumberModel {
    private String action;
    private String assetNumber;

    public AssetNumberModel(){
        super();
    }

    public AssetNumberModel(String action, String assetNumber) {
        super();
        this.action = action;
        this.assetNumber=assetNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }
}
