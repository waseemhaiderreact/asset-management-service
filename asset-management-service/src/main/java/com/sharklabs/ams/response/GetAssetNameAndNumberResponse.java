package com.sharklabs.ams.response;

import java.util.Map;

public class GetAssetNameAndNumberResponse {

    private Map<String,String> detail;

    private String responseIdentifier;

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
