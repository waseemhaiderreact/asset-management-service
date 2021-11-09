package com.sharklabs.ams.request;

import com.sharklabs.ams.asset.AssetExcelData;
import com.sharklabs.ams.field.FieldDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExportSampleExcelRequest {

    private AssetExcelData assetExcelData;

    public AssetExcelData getAssetExcelData() {
        return assetExcelData;
    }

    public void setAssetExcelData(AssetExcelData assetExcelData) {
        this.assetExcelData = assetExcelData;
    }
}
