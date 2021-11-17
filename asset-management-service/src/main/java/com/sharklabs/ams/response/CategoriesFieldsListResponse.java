package com.sharklabs.ams.response;

import com.sharklabs.ams.field.FieldDetailedDTO;

import java.util.List;

public class CategoriesFieldsListResponse {

    private List<FieldDetailedDTO> fieldDetailedDTOS;

    private String responseIdentifier;

    public List<FieldDetailedDTO> getFieldDetailedDTOS() {
        return fieldDetailedDTOS;
    }

    public void setFieldDetailedDTOS(List<FieldDetailedDTO> fieldDetailedDTOS) {
        this.fieldDetailedDTOS = fieldDetailedDTOS;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
