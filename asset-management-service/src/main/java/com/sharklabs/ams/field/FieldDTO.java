package com.sharklabs.ams.field;

public class FieldDTO {
    private String fieldLabel;

    private String fieldValue;

    public FieldDTO() {
    }

    public FieldDTO(String fieldLabel, String fieldValue) {
        this.fieldLabel = fieldLabel;
        this.fieldValue = fieldValue;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
