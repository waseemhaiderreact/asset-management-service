package com.sharklabs.ams.response;

import com.sharklabs.ams.asset.AssetNameAndUUIDModel;

import java.util.ArrayList;
import java.util.HashMap;

public class GetNameAndUUIDOfAssetResponse {
    private String responseIdentifier;
    private String responseCode;
    private ArrayList<AssetNameAndUUIDModel> assets=new ArrayList<>();

    public GetNameAndUUIDOfAssetResponse(){}

    public GetNameAndUUIDOfAssetResponse(String responseIdentifier, String responseCode, ArrayList<AssetNameAndUUIDModel>assets) {
        this.responseIdentifier = responseIdentifier;
        this.responseCode = responseCode;
        this.assets=assets;
    }


    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<AssetNameAndUUIDModel> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<AssetNameAndUUIDModel> assets) {
        this.assets = assets;
    }
}
