package com.sharklabs.ams.inspectionitemcategory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.inspectionitem.InspectionItem;
import com.sharklabs.ams.inspectiontemplate.InspectionTemplate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_insepction_item_category")
public class InspectionItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection_template_id",referencedColumnName = "id")
    private InspectionTemplate inspectionTemplate;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "inspectionItemCategory",fetch = FetchType.EAGER)
    private Set<InspectionItem> inspectionItems =new HashSet<>();

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

    public InspectionTemplate getInspectionTemplate() {
        return inspectionTemplate;
    }

    public void setInspectionTemplate(InspectionTemplate inspectionTemplate) {
        this.inspectionTemplate = inspectionTemplate;
    }

    public Set<InspectionItem> getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(Set<InspectionItem> inspectionItems) {
        this.inspectionItems = inspectionItems;
    }
}
