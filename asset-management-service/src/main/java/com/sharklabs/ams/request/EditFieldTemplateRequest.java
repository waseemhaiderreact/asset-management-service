package com.sharklabs.ams.request;

import com.sharklabs.ams.fieldtemplate.FieldTemplate;

public class EditFieldTemplateRequest {
    private FieldTemplate fieldTemplate;
    private String categoryId;

    public FieldTemplate getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplate fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
