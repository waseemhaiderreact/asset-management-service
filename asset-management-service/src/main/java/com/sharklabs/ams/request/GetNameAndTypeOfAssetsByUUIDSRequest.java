package com.sharklabs.ams.request;

import java.util.List;

public class GetNameAndTypeOfAssetsByUUIDSRequest {
    private List<String> uuids;

    public List<String> getUuids() {
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }
}
