package com.sharklabs.ams.request;

import com.sharklabs.ams.category.Category;

public class AddCategoryRequest {
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
