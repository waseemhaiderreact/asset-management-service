package com.sharklabs.ams.response;

import com.sharklabs.ams.category.CategoryDTO;

import java.io.Serializable;
import java.util.List;

public class CategoriesListResponse implements Serializable {

    private List<CategoryDTO> categoryDTOS;

    private String responseIdentifier;

    public CategoriesListResponse() {
    }

    public CategoriesListResponse(List<CategoryDTO> categoryDTOS, String responseIdentifier) {
        this.categoryDTOS = categoryDTOS;
        this.responseIdentifier = responseIdentifier;
    }

    public List<CategoryDTO> getCategoryDTOS() {
        return categoryDTOS;
    }

    public void setCategoryDTOS(List<CategoryDTO> categoryDTOS) {
        this.categoryDTOS = categoryDTOS;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
