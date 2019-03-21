package com.sharklabs.ams.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.category.Category;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_asset")
public class Asset implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<AssetField> assetFields =new HashSet<>();

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<AssetImage> assetImages =new HashSet<>();

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<AssetField> getAssetFields() {
        return assetFields;
    }

    public void setAssetFields(Set<AssetField> assetFields) {
        this.assetFields = assetFields;
    }

    public ActivityWall getActivityWall() {
        return activityWall;
    }

    public void setActivityWall(ActivityWall activityWall) {
        this.activityWall = activityWall;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public Set<AssetImage> getAssetImages() {
        return assetImages;
    }

    public void setAssetImages(Set<AssetImage> assetImages) {
        this.assetImages = assetImages;
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
}
