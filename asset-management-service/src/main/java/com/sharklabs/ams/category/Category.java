package com.sharklabs.ams.category;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;
import com.sharklabs.ams.inspectiontemplate.InspectionTemplate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name="t_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String name;

    private String description;

    private String iconUrl;

    private String tenantUUID;

    @OneToOne(mappedBy="category",cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private FieldTemplate fieldTemplate;

    @OneToOne(mappedBy="category",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private InspectionTemplate inspectionTemplate;

    @OneToMany( cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Set<Asset> assets =new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public FieldTemplate getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplate fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public InspectionTemplate getInspectionTemplate() {
        return inspectionTemplate;
    }

    public void setInspectionTemplate(InspectionTemplate inspectionTemplate) {
        this.inspectionTemplate = inspectionTemplate;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public void addAsset(Asset asset){
        this.assets.add(asset);
    }
}
