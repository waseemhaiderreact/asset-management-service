package com.sharklabs.ams.response;

import com.sharklabs.ams.category.Category;

public class GetCategoryResponse {
    private Category category;
    private String responseIdentifier;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
