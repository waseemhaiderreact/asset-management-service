package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetExcelData;

public class ImportExcelResponse {

    private AssetExcelData assetExcelData;

    private String responseIdentifier;

    public AssetExcelData getAssetExcelData() {
        return assetExcelData;
    }

    public void setAssetExcelData(AssetExcelData assetExcelData) {
        this.assetExcelData = assetExcelData;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
