package com.sharklabs.ams.category;

import com.sharklabs.ams.fieldtemplate.FieldTemplate;

public class CategoryFieldDTO {

    private String name;

    private String uuid;

    private FieldTemplate fieldTemplate;

    public CategoryFieldDTO() {
    }

    public CategoryFieldDTO(String name, String uuid, FieldTemplate fieldTemplate) {
        this.name = name;
        this.uuid = uuid;
        this.fieldTemplate = fieldTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public FieldTemplate getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplate fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }
}
