package com.sharklabs.ams.events.consumption;

public class ConsumptionModel {
    private String action;
    private Consumption consumption;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }
}
