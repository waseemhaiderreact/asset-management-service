package com.sharklabs.ams.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.AssetImage.AssetImage;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.attachment.Attachment;
import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.consumption.Consumption;
import com.sharklabs.ams.usage.Usage;

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

    private Date expiryDate;

    private String warranty;

    private String description;

    private String tenantUUID;

    private String primaryUsageUnit;

    private String secondaryUsageUnit;

    private String consumptionUnit;

    private int consumptionPoints;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<AssetField> assetFields =new HashSet<>();

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<AssetImage> assetImages =new HashSet<>();

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<Attachment> attachments =new HashSet<>();

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ActivityWall activityWall;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<Usage> usages =new HashSet<>();

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<Consumption> consumptions =new HashSet<>();

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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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

    public Set<Usage> getUsages() {
        return usages;
    }

    public void setUsages(Set<Usage> usages) {
        this.usages = usages;
    }

    public void addUsage(Usage usage){
        this.usages.add(usage);
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

    public Set<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(Set<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

    public void addConsumption(Consumption consumption){
        this.consumptions.add(consumption);
    }

    public String getConsumptionUnit() {
        return consumptionUnit;
    }

    public void setConsumptionUnit(String consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public int getConsumptionPoints() {
        return consumptionPoints;
    }

    public void setConsumptionPoints(int consumptionPoints) {
        this.consumptionPoints = consumptionPoints;
    }
}
