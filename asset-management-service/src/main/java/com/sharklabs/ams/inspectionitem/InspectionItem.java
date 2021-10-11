package com.sharklabs.ams.inspectionitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.inspectionitemcategory.InspectionItemCategory;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "t_inspection_item")
public class InspectionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String name;

    private String description;

    private String iconUrl;

    private String type;

    private String inspectionItemCategoryUUID;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JoinColumn(name = "inspection_item_category_id",referencedColumnName = "id")
//    private InspectionItemCategory inspectionItemCategory;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public InspectionItemCategory getInspectionItemCategory() {
//        return inspectionItemCategory;
//    }
//
//    public void setInspectionItemCategory(InspectionItemCategory inspectionItemCategory) {
//        this.inspectionItemCategory = inspectionItemCategory;
//    }

    public String getInspectionItemCategoryUUID() {
        return inspectionItemCategoryUUID;
    }

    public void setInspectionItemCategoryUUID(String inspectionItemCategoryUUID) {
        this.inspectionItemCategoryUUID = inspectionItemCategoryUUID;
    }
}
