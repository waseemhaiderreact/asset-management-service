package com.sharklabs.ams.request;

import java.util.List;

public class GetPaginatedConsumptionsByAssetsRequest {
    private List<String> assetUUIDS;
    private int offset;
    private int limit;

    public List<String> getAssetUUIDS() {
        return assetUUIDS;
    }

    public void setAssetUUIDS(List<String> assetUUIDS) {
        this.assetUUIDS = assetUUIDS;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
