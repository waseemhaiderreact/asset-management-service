package com.sharklabs.ams.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetIotDataResponse {
    Map<String,Object> response = new HashMap<>();
    private ArrayList<String> iotData = new ArrayList<>();

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }

    public ArrayList<String> getIotData() {
        return iotData;
    }

    public void setIotData(ArrayList<String> iotData) {
        this.iotData = iotData;
    }
}
