package com.sharklabs.ams.fieldtemplate;

import com.sharklabs.ams.field.Field;

import java.util.ArrayList;
import java.util.List;

public class FieldTemplateResponse {
    private Long id;

    private String uuid;

    private String type;

    private String tenantUUID;

    private boolean isPrivate;

    private List<Field> fields ;

    public void setFieldTemplate(FieldTemplate fieldTemplate){
        this.id=fieldTemplate.getId();
        this.uuid=fieldTemplate.getUuid();
        this.type=fieldTemplate.getType();
        this.tenantUUID=fieldTemplate.getTenantUUID();
        this.isPrivate=fieldTemplate.isPrivate();
        this.fields=new ArrayList<>(fieldTemplate.getFields());
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
