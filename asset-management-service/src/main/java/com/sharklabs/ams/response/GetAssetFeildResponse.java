package com.sharklabs.ams.response;

import java.util.HashMap;
import java.util.List;

public class GetAssetFeildResponse {
    HashMap<String,HashMap<String,String>> assetFields;
    String responseIdentifier;

    public HashMap<String, HashMap<String, String>> getAssetFields() {
        return assetFields;
    }

    public void setAssetFields(HashMap<String, HashMap<String, String>> assetFields) {
        this.assetFields = assetFields;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }


}
