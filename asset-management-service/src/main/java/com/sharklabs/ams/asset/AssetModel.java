package com.sharklabs.ams.asset;

import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//this model class will be used to send category uuid of asset alongwith asset
public class AssetModel {
    private Long id;

    private String assetNumber;

    private String uuid;

    private String name;

    private String modelNumber;

    private String inventory;

    private String manufacture;

    private Date purchaseDate;

    private String warranty;

    private String description;

    private String tenantUUID;

    private String categoryUUID;

    private Set<AssetField> assetFields =new HashSet<>();

    private Set<AssetImage> assetImages =new HashSet<>();

    private ActivityWall activityWall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    public Set<AssetField> getAssetFields() {
        return assetFields;
    }

    public void setAssetFields(Set<AssetField> assetFields) {
        this.assetFields = assetFields;
    }

    public Set<AssetImage> getAssetImages() {
        return assetImages;
    }

    public void setAssetImages(Set<AssetImage> assetImages) {
        this.assetImages = assetImages;
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
        this.assetFields=asset.getAssetFields();
        this.assetImages=asset.getAssetImages();
        this.assetNumber=asset.getAssetNumber();
        this.categoryUUID=asset.getCategory().getUuid();
        this.description=asset.getDescription();
        this.inventory=asset.getInventory();
        this.manufacture=asset.getManufacture();
        this.modelNumber=asset.getModelNumber();
        this.name=asset.getName();
        this.purchaseDate=asset.getPurchaseDate();
        this.tenantUUID=asset.getTenantUUID();
        this.uuid=asset.getUuid();
        this.warranty=asset.getWarranty();
    }
}
