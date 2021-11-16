package com.sharklabs.ams.category;

import com.sharklabs.ams.field.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CategoryDTO implements Serializable {

    private String uuid;

    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
