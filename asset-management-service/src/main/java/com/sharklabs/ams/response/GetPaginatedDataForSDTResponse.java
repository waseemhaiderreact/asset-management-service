package com.sharklabs.ams.response;

import java.util.HashMap;
import java.util.Map;

public class GetPaginatedDataForSDTResponse {
    private Map<String,Object> sdtData = new HashMap<>();
    private String responseIdentifier;

    public Map<String, Object> getSdtData() {
        return sdtData;
    }

    public void setSdtData(Map<String, Object> sdtData) {
        this.sdtData = sdtData;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
