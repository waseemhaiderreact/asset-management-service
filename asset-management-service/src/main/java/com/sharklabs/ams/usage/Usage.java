package com.sharklabs.ams.usage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.asset.Asset;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "t_usages")
public class Usage{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String primaryUsageValue;
    private String secondaryUsageValue;
    private String primaryUsageUnit;
    private String secondaryUsageUnit;
    private Date primaryUsageTime;
    private Double primaryUsageLat;
    private Double primaryUsageLng;
    private Date secondaryUsageTime;
    private Double secondaryUsageLat;
    private Double secondaryUsageLng;

    private String assetUUID; //asset uuid (the asset of which these usage values will be stored)

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_id",referencedColumnName = "id")
    private Asset asset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimaryUsageValue() {
        return primaryUsageValue;
    }

    public void setPrimaryUsageValue(String primaryUsageValue) {
        this.primaryUsageValue = primaryUsageValue;
    }

    public String getSecondaryUsageValue() {
        return secondaryUsageValue;
    }

    public void setSecondaryUsageValue(String secondaryUsageValue) {
        this.secondaryUsageValue = secondaryUsageValue;
    }

    public String getPrimaryUsageUnit() {
        return primaryUsageUnit;
    }

    public void setPrimaryUsageUnit(String primaryUsageUnit) {
        this.primaryUsageUnit = primaryUsageUnit;
    }

    public String getSecondaryUsageUnit() {
        return secondaryUsageUnit;
    }

    public void setSecondaryUsageUnit(String secondaryUsageUnit) {
        this.secondaryUsageUnit = secondaryUsageUnit;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    public Date getPrimaryUsageTime() {
        return primaryUsageTime;
    }

    public void setPrimaryUsageTime(Date primaryUsageTime) {
        this.primaryUsageTime = primaryUsageTime;
    }

    public Double getPrimaryUsageLat() {
        return primaryUsageLat;
    }

    public void setPrimaryUsageLat(Double primaryUsageLat) {
        this.primaryUsageLat = primaryUsageLat;
    }

    public Double getPrimaryUsageLng() {
        return primaryUsageLng;
    }

    public void setPrimaryUsageLng(Double primaryUsageLng) {
        this.primaryUsageLng = primaryUsageLng;
    }

    public Date getSecondaryUsageTime() {
        return secondaryUsageTime;
    }

    public void setSecondaryUsageTime(Date secondaryUsageTime) {
        this.secondaryUsageTime = secondaryUsageTime;
    }

    public Double getSecondaryUsageLat() {
        return secondaryUsageLat;
    }

    public void setSecondaryUsageLat(Double secondaryUsageLat) {
        this.secondaryUsageLat = secondaryUsageLat;
    }

    public Double getSecondaryUsageLng() {
        return secondaryUsageLng;
    }

    public void setSecondaryUsageLng(Double secondaryUsageLng) {
        this.secondaryUsageLng = secondaryUsageLng;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

}
