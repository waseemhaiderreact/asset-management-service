package com.sharklabs.ams.request;

public class AssetDetailRequest {


    private String uuid;
    private boolean assetFields;
    private boolean assetImages;
    private boolean attachments;
    private boolean activityWall;
    private boolean usages;
    private boolean consumptions;
    private boolean category;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isAssetFields() {
        return assetFields;
    }

    public void setAssetFields(boolean assetFields) {
        this.assetFields = assetFields;
    }

    public boolean isAssetImages() {
        return assetImages;
    }

    public void setAssetImages(boolean assetImages) {
        this.assetImages = assetImages;
    }

    public boolean isAttachments() {
        return attachments;
    }

    public void setAttachments(boolean attachments) {
        this.attachments = attachments;
    }

    public boolean isActivityWall() {
        return activityWall;
    }

    public void setActivityWall(boolean activityWall) {
        this.activityWall = activityWall;
    }

    public boolean isUsages() {
        return usages;
    }

    public void setUsages(boolean usages) {
        this.usages = usages;
    }

    public boolean isConsumptions() {
        return consumptions;
    }

    public void setConsumptions(boolean consumptions) {
        this.consumptions = consumptions;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }
}
