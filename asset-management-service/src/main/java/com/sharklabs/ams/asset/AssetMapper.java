package com.sharklabs.ams.asset;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "t_asset_mapper")
public class AssetMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String assetNumber;

    private String name;

    private String categoryName;

    private String status;

    @Column(columnDefinition = "varchar(255) default '0'")
    private String openIssues;

    @Column(columnDefinition = "varchar(255) default '0'")
    private String assignedIssues;

    @Column(columnDefinition = "varchar(255) default '0'")
    private String repairs;

    @Column(columnDefinition = "varchar(255) default '0'")
    private String workorders;

    private String assignedTo;

    @Column(columnDefinition = "varchar(255) default '0'")
    private String maintenanceCost;

    private Boolean archive;

    private String removeFromCategoryUUID;

    private String tenantUUID;

    public AssetMapper(String uuid, String assetNumber, String name, String categoryName, String status, int archive, String removeFromCategoryUUID, String tenantUUID) {
        this.uuid = uuid;
        this.assetNumber = assetNumber;
        this.name = name;
        this.categoryName = categoryName;
        this.status = status;
        this.archive = (archive == 0 ? false: true);
        this.removeFromCategoryUUID = removeFromCategoryUUID;
        this.tenantUUID = tenantUUID;
        this.maintenanceCost = "0";
        this.assignedIssues = "0";
        this.openIssues = "0";
        this.repairs = "0";
        this.workorders = "0";
    }

    public AssetMapper() {

    }

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

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(String openIssues) {
        this.openIssues = openIssues;
    }

    public String getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(String assignedIssues) {
        this.assignedIssues = assignedIssues;
    }

    public String getRepairs() {
        return repairs;
    }

    public void setRepairs(String repairs) {
        this.repairs = repairs;
    }

    public String getWorkorders() {
        return workorders;
    }

    public void setWorkorders(String workorders) {
        this.workorders = workorders;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(String maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public String getRemoveFromCategoryUUID() {
        return removeFromCategoryUUID;
    }

    public void setRemoveFromCategoryUUID(String removeFromCategoryUUID) {
        this.removeFromCategoryUUID = removeFromCategoryUUID;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }
}
