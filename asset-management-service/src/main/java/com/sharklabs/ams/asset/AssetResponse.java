package com.sharklabs.ams.asset;

import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;

import java.util.HashMap;
import java.util.Set;

public class AssetResponse {
    private Long id;

    private String uuid;

    private String imageUrl;

    private String name;

    private String tenantUUID;

    private HashMap<String, Object> assetFields=new HashMap<String, Object>();

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

    public void setAssetFields(HashMap assetFields) {
        this.assetFields = assetFields;
    }

    public ActivityWall getActivityWall() {
        return activityWall;
    }

    public void setActivityWall(ActivityWall activityWall) {
        this.activityWall = activityWall;
    }

    public void setAsset(Asset asset){
        this.id=asset.getId();
        this.activityWall=asset.getActivityWall();
        this.imageUrl=asset.getImageUrl();
        this.name=asset.getName();
        this.tenantUUID=asset.getTenantUUID();
        this.uuid=asset.getUuid();
        Set<AssetField> assetFields=asset.getAssetFields();
        for(AssetField assetField: assetFields){
            this.assetFields.put(assetField.getFieldId(),assetField);
        }
    }
}
