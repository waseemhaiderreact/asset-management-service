package com.sharklabs.ams.request;

import com.sharklabs.ams.consumption.Consumption;
import com.sharklabs.ams.imagevoice.ImageVoice;

import java.util.List;

public class AddConsumptionUnitsRequest {
    private Consumption consumption;
    private String assetUUID;
    private List<ImageVoice> imageVoices;
    private String userUUID;
    public String getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
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
    public List<ImageVoice> getImageVoices() {
        return imageVoices;
    }
    public void setImageVoices(List<ImageVoice> imageVoices) {
        this.imageVoices = imageVoices;
    }
}
