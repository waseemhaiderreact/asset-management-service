package com.sharklabs.ams.request;

import java.util.ArrayList;

public class GetNameAndTypeOfAssetsByUUIDSRequest {
    private ArrayList<String> uuids=new ArrayList<>();

    public ArrayList<String> getUuids() {
        return uuids;
    }

    public void setUuids(ArrayList<String> uuids) {
        this.uuids = uuids;
    }
}
