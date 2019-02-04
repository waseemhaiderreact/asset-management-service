package com.sharklabs.ams.response;

import com.sharklabs.ams.inspectiontemplate.InspectionTemplate;

public class EditInspectionTemplateResponse {
    private InspectionTemplate inspectionTemplate;
    private String responseIdentifier;

    public InspectionTemplate getInspectionTemplate() {
        return inspectionTemplate;
    }

    public void setInspectionTemplate(InspectionTemplate inspectionTemplate) {
        this.inspectionTemplate = inspectionTemplate;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
