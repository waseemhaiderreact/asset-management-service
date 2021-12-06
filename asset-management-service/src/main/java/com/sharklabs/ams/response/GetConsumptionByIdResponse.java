package com.sharklabs.ams.response;

import com.sharklabs.ams.consumption.Consumption;
import com.sharklabs.ams.imagevoice.ImageVoice;

import java.util.List;

public class GetConsumptionByIdResponse {
    private String responseIdentifier;
    private Consumption consumption;

    public List<ImageVoice> getImageVoices() {
        return imageVoices;
    }

    public void setImageVoices(List<ImageVoice> imageVoices) {
        this.imageVoices = imageVoices;
    }

    private List<ImageVoice> imageVoices;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }
}
