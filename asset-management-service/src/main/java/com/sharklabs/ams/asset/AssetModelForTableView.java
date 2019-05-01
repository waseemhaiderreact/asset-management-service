package com.sharklabs.ams.asset;

public class AssetModelForTableView {
    private Long id;
    private String assetNumber;

    public AssetModelForTableView() {
    }
    public AssetModelForTableView(Long id, String assetNumber) {
        this.id = id;
        this.assetNumber = assetNumber;
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


}
