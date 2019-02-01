package com.sharklabs.ams.response;

import com.sharklabs.ams.fieldtemplate.FieldTemplate;

public class GetFieldTemplateResponse {
    private FieldTemplate fieldTemplate;
    private String responseIdentifier;

    public FieldTemplate getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(FieldTemplate fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
