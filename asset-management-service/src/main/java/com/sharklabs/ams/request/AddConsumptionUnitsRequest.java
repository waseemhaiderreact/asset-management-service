package com.sharklabs.ams.request;

import com.sharklabs.ams.consumption.Consumption;

public class AddConsumptionUnitsRequest {
    private Consumption consumption;
    private String assetUUID;

    public Consumption getConsumption() {
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }
}
