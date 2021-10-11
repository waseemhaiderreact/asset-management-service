package com.sharklabs.ams.request;

public class EditConsumptionRequest {

    private String uuid;

    private String consumptionValue;

    private Long price;

    private String currency;

    private int updatedConsumptionPoints;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConsumptionValue() {
        return consumptionValue;
    }

    public void setConsumptionValue(String consumptionValue) {
        this.consumptionValue = consumptionValue;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getUpdatedConsumptionPoints() {
        return updatedConsumptionPoints;
    }

    public void setUpdatedConsumptionPoints(int updatedConsumptionPoints) {
        this.updatedConsumptionPoints = updatedConsumptionPoints;
    }
}
