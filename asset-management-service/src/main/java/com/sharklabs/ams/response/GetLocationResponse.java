package com.sharklabs.ams.response;

import java.util.HashMap;
import java.util.Map;

public class GetLocationResponse {

    Map<String,Object> response = new HashMap<>();

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }
}
