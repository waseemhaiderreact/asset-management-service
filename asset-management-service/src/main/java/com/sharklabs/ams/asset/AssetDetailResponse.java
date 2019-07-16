package com.sharklabs.ams.asset;

import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.attachment.Attachment;
import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.consumption.Consumption;
import com.sharklabs.ams.usage.Usage;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AssetDetailResponse {
    private AssetDetail assetDetail;

    private Set<Attachment> attachment = new HashSet<>();

    private Set<Consumption> consumption = new HashSet<>();

    private Set<Usage> Usage = new HashSet<>();

    private Set<AssetField> assetField = new HashSet<>();

    private Set<AssetImage> assetImage = new HashSet<>();

    private ActivityWall activityWall;

    private Category category;

    public AssetDetail getAssetDetail() {
        return assetDetail;
    }

    public void setAssetDetail(AssetDetail assetDetail) {
        this.assetDetail = assetDetail;
    }

    public Set<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(Set<Attachment> attachment) {
        this.attachment = attachment;
    }

    public Set<Consumption> getConsumption() {
        return consumption;
    }

    public void setConsumption(Set<Consumption> consumption) {
        this.consumption = consumption;
    }

    public Set<com.sharklabs.ams.usage.Usage> getUsage() {
        return Usage;
    }

    public void setUsage(Set<com.sharklabs.ams.usage.Usage> usage) {
        Usage = usage;
    }

    public Set<AssetField> getAssetField() {
        return assetField;
    }

    public void setAssetField(Set<AssetField> assetField) {
        this.assetField = assetField;
    }

    public Set<AssetImage> getAssetImage() {
        return assetImage;
    }

    public void setAssetImage(Set<AssetImage> assetImage) {
        this.assetImage = assetImage;
    }

    public ActivityWall getActivityWall() {
        return activityWall;
    }

    public void setActivityWall(ActivityWall activityWall) {
        this.activityWall = activityWall;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
