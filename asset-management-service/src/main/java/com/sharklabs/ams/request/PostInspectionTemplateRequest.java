package com.sharklabs.ams.request;

import com.sharklabs.ams.inspectiontemplate.InspectionTemplate;

public class PostInspectionTemplateRequest {
    private InspectionTemplate inspectionTemplate;
    private String categoryId;

    public InspectionTemplate getInspectionTemplate() {
        return inspectionTemplate;
    }

    public void setInspectionTemplate(InspectionTemplate inspectionTemplate) {
        this.inspectionTemplate = inspectionTemplate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
