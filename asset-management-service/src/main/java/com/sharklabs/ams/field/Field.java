package com.sharklabs.ams.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "t_field")
public class Field implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String label;

    private String fieldMetadata; // will hold JSON

    private String type;

    private String iconUrl;

    private int fieldPosition;

    private boolean isMandatory;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "field_template_id",referencedColumnName = "id")
    private FieldTemplate fieldTemplate;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFieldMetadata() {
        return fieldMetadata;
    }

    public void setFieldMetadata(String fieldMetadata) {
        this.fieldMetadata = fieldMetadata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public FieldTemplate getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplate fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }
}
