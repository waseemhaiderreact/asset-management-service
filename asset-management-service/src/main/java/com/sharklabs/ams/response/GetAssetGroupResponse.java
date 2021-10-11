package com.sharklabs.ams.response;

import com.sharklabs.ams.assetGroup.AssetGroup;

import java.util.HashMap;

public class GetAssetGroupResponse {

    private HashMap<String,Object> assetGroup;
    private String responseIdentifier;

    public GetAssetGroupResponse(){

    }

    public GetAssetGroupResponse(String responseIdentifier, HashMap<String,Object> assetGroup){
        this.responseIdentifier=responseIdentifier;
        this.assetGroup=assetGroup;
    }

    public HashMap<String,Object> getAssetGroup() {
        return assetGroup;
    }

    public void setAssetGroup(HashMap<String,Object> assetGroup) {
        this.assetGroup = assetGroup;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
