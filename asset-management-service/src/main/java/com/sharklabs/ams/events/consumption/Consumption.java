package com.sharklabs.ams.events.consumption;

public class Consumption {
    private String primaryConsumptionValue;
    private String secondaryConsumptionValue;
    private String primaryConsumptionUnit;
    private String secondaryConsumptionUnit;

    private String assetUUID; //asset uuid (the asset of which these consumption values will be stored)

    public String getPrimaryConsumptionValue() {
        return primaryConsumptionValue;
    }

    public void setPrimaryConsumptionValue(String primaryConsumptionValue) {
        this.primaryConsumptionValue = primaryConsumptionValue;
    }

    public String getSecondaryConsumptionValue() {
        return secondaryConsumptionValue;
    }

    public void setSecondaryConsumptionValue(String secondaryConsumptionValue) {
        this.secondaryConsumptionValue = secondaryConsumptionValue;
    }

    public String getPrimaryConsumptionUnit() {
        return primaryConsumptionUnit;
    }

    public void setPrimaryConsumptionUnit(String primaryConsumptionUnit) {
        this.primaryConsumptionUnit = primaryConsumptionUnit;
    }

    public String getSecondaryConsumptionUnit() {
        return secondaryConsumptionUnit;
    }

    public void setSecondaryConsumptionUnit(String secondaryConsumptionUnit) {
        this.secondaryConsumptionUnit = secondaryConsumptionUnit;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    //should this model be sent to asset management service
    public boolean shouldBeSent(){
        if((this.primaryConsumptionValue!=null && this.primaryConsumptionUnit!=null) || (this.secondaryConsumptionUnit!=null && this.secondaryConsumptionValue!=null)){
            return true;
        }
        else{
            return false;
        }
    }
}
