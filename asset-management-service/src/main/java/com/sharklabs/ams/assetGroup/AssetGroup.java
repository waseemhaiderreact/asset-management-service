package com.sharklabs.ams.assetGroup;

import com.sharklabs.ams.asset.Asset;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "t_asset_groups")
public class AssetGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uuid;
    private String createdByUserUUID;
    private String createdByUserName;
    private String AssetCategory;
    private Date createdAt;
    private String groupName;
    private String tenantUUID;
    private String lastEditedByUserUUID;
    private String lastEditedByUserName;
    private int totalMembers;
    private boolean archive;
    private String deletefromGroupUUID;



    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_asset_group_members",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "asset_id")}
    )
    private Set<Asset> assets;

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

    public String getCreatedByUserUUID() {
        return createdByUserUUID;
    }

    public void setCreatedByUserUUID(String createdByUserUUID) {
        this.createdByUserUUID = createdByUserUUID;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public String getAssetCategory() {    return AssetCategory; }

    public void setAssetCategory(String assetCategory) {  AssetCategory = assetCategory; }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getLastEditedByUserUUID() {
        return lastEditedByUserUUID;
    }

    public void setLastEditedByUserUUID(String lastEditedByUserUUID) {
        this.lastEditedByUserUUID = lastEditedByUserUUID;
    }

    public String getLastEditedByUserName() {
        return lastEditedByUserName;
    }

    public void setLastEditedByUserName(String lastEditedByUserName) {
        this.lastEditedByUserName = lastEditedByUserName;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public String getDeletefromGroupUUID() {
        return deletefromGroupUUID;
    }

    public void setDeletefromGroupUUID(String deletefromGroupUUID) {
        this.deletefromGroupUUID = deletefromGroupUUID;
    }
}
