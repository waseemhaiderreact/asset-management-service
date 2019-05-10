package com.sharklabs.ams.response;

import com.sharklabs.ams.consumption.Consumption;

public class GetConsumptionByIdResponse {
    private String responseIdentifier;
    private Consumption consumption;

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
