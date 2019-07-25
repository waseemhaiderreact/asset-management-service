package com.sharklabs.ams.response;

import java.util.ArrayList;
import java.util.List;

public class GetAssetUUIDsByNameResponse {
    List<Object> assetUUIDs;
    String responseIdentifier;

    public List<Object> getAssetUUIDs() {
        return assetUUIDs;
    }

    public void setAssetUUIDs(List<Object> assetUUIDs) {
        this.assetUUIDs = assetUUIDs;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
