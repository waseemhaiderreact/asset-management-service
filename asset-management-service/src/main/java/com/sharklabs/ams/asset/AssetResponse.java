package com.sharklabs.ams.asset;

import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.usage.Usage;

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

    private String primaryUsageUnit;

    private String secondaryUsageUnit;

    private Usage usage;

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

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
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

    public void setAsset(Asset asset){
        this.id=asset.getId();
        this.activityWall=asset.getActivityWall();
        this.name=asset.getName();
        this.tenantUUID=asset.getTenantUUID();
        this.uuid=asset.getUuid();
        this.description=asset.getDescription();
        this.primaryUsageUnit=asset.getPrimaryUsageUnit();
        this.secondaryUsageUnit=asset.getSecondaryUsageUnit();
        //getting latest entry of the usage of asset
        Usage maxUsage=null;
        Long maxId=null;
        for(Usage usage: asset.getUsages()){
            if(maxId==null){
                maxUsage=usage;
                maxId=usage.getId();
            }
            else{
                if(usage.getId()>maxId){
                    maxUsage=usage;
                    maxId=usage.getId();
                }
            }
        }
        this.usage=maxUsage;
        Set<AssetField> assetFields=asset.getAssetFields();
        for(AssetField assetField: assetFields){
            this.assetFields.put(assetField.getFieldId(),assetField);
        }
        this.assetImages=asset.getAssetImages();
    }
}
