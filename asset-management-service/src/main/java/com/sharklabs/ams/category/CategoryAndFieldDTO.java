package com.sharklabs.ams.category;

import com.sharklabs.ams.field.FieldDetailDTO;

import java.util.List;

public class CategoryAndFieldDTO {

    private String uuid;

    private String name;

    private List<FieldDetailDTO> fieldDetailDTOS;

    public CategoryAndFieldDTO() {
    }

    public CategoryAndFieldDTO(String uuid, String name) {
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

    public List<FieldDetailDTO> getFieldDetailDTOS() {
        return fieldDetailDTOS;
    }

    public void setFieldDetailDTOS(List<FieldDetailDTO> fieldDetailDTOS) {
        this.fieldDetailDTOS = fieldDetailDTOS;
    }
}
