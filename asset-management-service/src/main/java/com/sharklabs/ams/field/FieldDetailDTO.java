package com.sharklabs.ams.field;

import java.io.Serializable;

public class FieldDetailDTO implements Serializable {

    private String uuid;

    private String label;

    private String fieldMetadata; // will hold JSON

    private String type;


    private String options;

    private String iconUrl;

    private int fieldPosition;

    private boolean isMandatory;

    private String fieldTemplateUUID;

    public FieldDetailDTO(String uuid, String label, String fieldMetadata, String type, String options, String iconUrl, int fieldPosition, boolean isMandatory, String fieldTemplateUUID) {
        this.uuid = uuid;
        this.label = label;
        this.fieldMetadata = fieldMetadata;
        this.type = type;
        this.options = options;
        this.iconUrl = iconUrl;
        this.fieldPosition = fieldPosition;
        this.isMandatory = isMandatory;
        this.fieldTemplateUUID = fieldTemplateUUID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFieldMetadata() {
        return fieldMetadata;
    }

    public void setFieldMetadata(String fieldMetadata) {
        this.fieldMetadata = fieldMetadata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getFieldTemplateUUID() {
        return fieldTemplateUUID;
    }

    public void setFieldTemplateUUID(String fieldTemplateUUID) {
        this.fieldTemplateUUID = fieldTemplateUUID;
    }
}
