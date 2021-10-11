package com.sharklabs.ams.response;

import java.util.List;
import java.util.Map;

public class GetUserAssetsResponse {

    private String responseIdentifier;

    private List<Map<String,String>> assets;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public List<Map<String, String>> getAssets() {
        return assets;
    }

    public void setAssets(List<Map<String, String>> assets) {
        this.assets = assets;
    }
}
