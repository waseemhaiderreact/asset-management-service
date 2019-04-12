package com.sharklabs.ams.asset;

import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AssetResponse {
    private Long id;

    private String uuid;

    private String imageUrl;

    private String name;

    private String description;

    private String tenantUUID;

    private String primaryConsumptionValue;

    private String primaryConsumptionUnit;

    private String secondaryConsumptionValue;

    private String secondaryConsumptionUnit;

    private HashMap<String, Object> assetFields=new HashMap<String, Object>();

    private Set<AssetImage> assetImages=new HashSet<>();

    private ActivityWall activityWall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public HashMap getAssetFields() {
        return assetFields;
    }


    public ActivityWall getActivityWall() {
        return activityWall;
    }

    public void setActivityWall(ActivityWall activityWall) {
        this.activityWall = activityWall;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssetFields(HashMap<String, Object> assetFields) {
        this.assetFields = assetFields;
    }

    public Set<AssetImage> getAssetImages() {
        return assetImages;
    }

    public void setAssetImages(Set<AssetImage> assetImages) {
        this.assetImages = assetImages;
    }

    public String getPrimaryConsumptionValue() {
        return primaryConsumptionValue;
    }

    public void setPrimaryConsumptionValue(String primaryConsumptionValue) {
        this.primaryConsumptionValue = primaryConsumptionValue;
    }

    public String getPrimaryConsumptionUnit() {
        return primaryConsumptionUnit;
    }

    public void setPrimaryConsumptionUnit(String primaryConsumptionUnit) {
        this.primaryConsumptionUnit = primaryConsumptionUnit;
    }

    public String getSecondaryConsumptionValue() {
        return secondaryConsumptionValue;
    }

    public void setSecondaryConsumptionValue(String secondaryConsumptionValue) {
        this.secondaryConsumptionValue = secondaryConsumptionValue;
    }

    public String getSecondaryConsumptionUnit() {
        return secondaryConsumptionUnit;
    }

    public void setSecondaryConsumptionUnit(String secondaryConsumptionUnit) {
        this.secondaryConsumptionUnit = secondaryConsumptionUnit;
    }

    public void setAsset(Asset asset){
        this.id=asset.getId();
        this.activityWall=asset.getActivityWall();
        this.name=asset.getName();
        this.tenantUUID=asset.getTenantUUID();
        this.uuid=asset.getUuid();
        this.description=asset.getDescription();
        this.primaryConsumptionUnit=asset.getPrimaryConsumptionUnit();
        this.primaryConsumptionValue=asset.getPrimaryConsumptionValue();
        this.secondaryConsumptionUnit=asset.getSecondaryConsumptionUnit();
        this.secondaryConsumptionValue=asset.getSecondaryConsumptionValue();
        Set<AssetField> assetFields=asset.getAssetFields();
        for(AssetField assetField: assetFields){
            this.assetFields.put(assetField.getFieldId(),assetField);
        }
        this.assetImages=asset.getAssetImages();
    }
}
