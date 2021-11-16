package com.sharklabs.ams.response;

import com.sharklabs.ams.category.CategoryAndFieldDTO;

import java.util.List;

public class CategoriesFieldsListResponse {

    private List<CategoryAndFieldDTO> categoryAndFieldDTOS;

    private String responseIdentifier;

    public List<CategoryAndFieldDTO> getCategoryAndFieldDTOS() {
        return categoryAndFieldDTOS;
    }

    public void setCategoryAndFieldDTOS(List<CategoryAndFieldDTO> categoryAndFieldDTOS) {
        this.categoryAndFieldDTOS = categoryAndFieldDTOS;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
