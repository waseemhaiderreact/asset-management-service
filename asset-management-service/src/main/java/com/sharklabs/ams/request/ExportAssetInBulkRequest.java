package com.sharklabs.ams.request;

import java.util.List;

public class ExportAssetInBulkRequest {

    private List<String> assetUUIDS;

    private String tenantUUID;

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public List<String> getAssetUUIDS() {
        return assetUUIDS;
    }

    public void setAssetUUIDS(List<String> assetUUIDS) {
        this.assetUUIDS = assetUUIDS;
    }
}
