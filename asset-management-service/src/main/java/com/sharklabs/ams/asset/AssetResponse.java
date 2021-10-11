package com.sharklabs.ams.asset;

import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.attachment.Attachment;
import com.sharklabs.ams.usage.Usage;

import java.util.*;

public class AssetResponse {
    private Long id;

    private String uuid;

    private String imageUrl;

    private String name;

    private String description;

    private String modelNumber;

    private String inventory;

    private String manufacture;

    private Date purchaseDate;

    private String warranty;

    private String tenantUUID;

    private String categoryUUID;

    private String primaryUsageUnit;

    private String secondaryUsageUnit;

    private String consumptionUnit;

    private String status;

    private Usage usage;

    private Usage lastUsage;

    private String assetNumber;

    private HashMap<String, Object> assetFields=new HashMap<String, Object>();

    private Set<AssetImage> assetImages=new HashSet<>();

    private Set<Attachment> attachments=new HashSet<>();

    private ActivityWall activityWall;

    private int consumptionPoints;

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

    public String getConsumptionUnit() {
        return consumptionUnit;
    }

    public void setConsumptionUnit(String consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }

    public String getStatus() {   return status;   }

    public void setStatus(String status) {   this.status = status;   }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setAsset(Asset asset){
        this.id=asset.getId();
//        this.activityWall=asset.getActivityWall();
        this.name=asset.getName();
        this.tenantUUID=asset.getTenantUUID();
        this.uuid=asset.getUuid();
        this.categoryUUID = asset.getCategoryUUID();
        this.description=asset.getDescription();
        this.primaryUsageUnit=asset.getPrimaryUsageUnit();
        this.secondaryUsageUnit=asset.getSecondaryUsageUnit();
        this.consumptionUnit=asset.getConsumptionUnit();
        this.manufacture=asset.getManufacture();
        this.modelNumber=asset.getModelNumber();
        this.purchaseDate=asset.getPurchaseDate();
        this.warranty=asset.getWarranty();
        //getting latest entry of the usage of asset
        Usage maxUsage=null;
        Long maxId=null;
        for(Usage usage: asset.getUsages()){
            if(maxId==null){
                maxUsage=usage;
                maxId=usage.getId();
            }
            else{
                if(usage.getId()>maxId && usage.getPrimaryUsageValue()!=null){
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
        this.attachments=asset.getAttachments();
        this.consumptionPoints=asset.getConsumptionPoints();
        this.assetNumber = asset.getAssetNumber();
    }

    public int getConsumptionPoints() {
        return consumptionPoints;
    }

    public void setConsumptionPoints(int consumptionPoints) {
        this.consumptionPoints = consumptionPoints;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public Usage getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Usage lastUsage) {
        this.lastUsage = lastUsage;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }
}
