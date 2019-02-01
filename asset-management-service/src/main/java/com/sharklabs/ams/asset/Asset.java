package com.sharklabs.ams.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.activitywall.ActivityWall;
import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.category.Category;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_asset")
public class Asset implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String imageUrl;

    private String name;

    private String tenantUUID;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "asset",fetch = FetchType.EAGER)
    private Set<AssetField> assetFields =new HashSet<>();

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
}
