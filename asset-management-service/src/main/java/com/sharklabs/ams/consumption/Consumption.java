package com.sharklabs.ams.consumption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.asset.Asset;


import javax.persistence.*;
import java.util.Date;

@Entity(name="t_consumption")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String consumptionValue;

    private Long price;

    private String currency;

    @Column(nullable =true)
    private int updatedConsumptionLevel;
    private int updatedConsumptionPoints;

    //meter fields
    private String meterType;
    private String meterValue;

    //location
    private Double lat;
    private Double lng;

    private Date createdAt;

    private String tenantUUID;

    private String assetUUID;

    private String submittedBy;

    private String consumptionUnit;

    private String assetName;

    private String assetNumber;

    private String assetCategory;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE})
//    @JoinColumn(name = "asset_id",referencedColumnName = "id")
//    private Asset asset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsumptionValue() {
        return consumptionValue;
    }

    public void setConsumptionValue(String consumptionValue) {
        this.consumptionValue = consumptionValue;
    }

//    public Asset getAsset() {
//        return asset;
//    }
//
//    public void setAsset(Asset asset) {
//        this.asset = asset;
//    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(String meterValue) {
        this.meterValue = meterValue;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getUpdatedConsumptionLevel() {
        return updatedConsumptionLevel;
    }

    public void setUpdatedConsumptionLevel(int updatedConsumptionLevel) {
        this.updatedConsumptionLevel = updatedConsumptionLevel;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getConsumptionUnit() {
        return consumptionUnit;
    }

    public void setConsumptionUnit(String consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }
}
