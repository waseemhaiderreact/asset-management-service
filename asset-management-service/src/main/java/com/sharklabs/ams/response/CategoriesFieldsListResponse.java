package com.sharklabs.ams.response;

import com.sharklabs.ams.category.CategoryFieldDTO;

import java.util.List;

public class CategoriesFieldsListResponse {

    private List<CategoryFieldDTO> categoryFieldDTOS;

    private String responseIdentifier;

    public List<CategoryFieldDTO> getCategoryFieldDTOS() {
        return categoryFieldDTOS;
    }

    public void setCategoryFieldDTOS(List<CategoryFieldDTO> categoryFieldDTOS) {
        this.categoryFieldDTOS = categoryFieldDTOS;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
