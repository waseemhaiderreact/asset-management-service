package com.sharklabs.ams.response;

import com.sharklabs.ams.category.Category;

import java.util.List;

public class GetCategoriesResponse {
    private List<Category> categories;
    private String responseIdentifier;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
