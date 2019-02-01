package com.sharklabs.ams.inspectiontemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.category.Category;
import com.sharklabs.ams.inspectionitemcategory.InspectionItemCategory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_inspection_template")
public class InspectionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String type;

    private String tenantUUID;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "inspectionTemplate",fetch = FetchType.EAGER)
    private Set<InspectionItemCategory> inspectionItemCategories =new HashSet<>();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Set<InspectionItemCategory> getInspectionItemCategories() {
        return inspectionItemCategories;
    }

    public void setInspectionItemCategories(Set<InspectionItemCategory> inspectionItemCategories) {
        this.inspectionItemCategories = inspectionItemCategories;
    }
}
