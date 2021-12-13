package com.sharklabs.ams.importtemplate;

public class ImportTemplateDTO {

    private String uuid;

    private byte[] csvColumnData;

    private String columnData;

    private String templateName;

    private String templateNumber;

    private String categoryUUID;

    private String categoryName;

    public ImportTemplateDTO() {
    }

    public ImportTemplateDTO(String uuid, byte[] csvColumnData, String templateName, String templateNumber, String categoryUUID, String categoryName) {
        this.uuid = uuid;
        this.csvColumnData = csvColumnData;
        this.templateName = templateName;
        this.templateNumber = templateNumber;
        this.categoryUUID = categoryUUID;
        this.categoryName = categoryName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getCsvColumnData() {
        return csvColumnData;
    }

    public void setCsvColumnData(byte[] csvColumnData) {
        this.csvColumnData = csvColumnData;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateNumber() {
        return templateNumber;
    }

    public void setTemplateNumber(String templateNumber) {
        this.templateNumber = templateNumber;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColumnData() {
        return columnData;
    }

    public void setColumnData(String columnData) {
        this.columnData = columnData;
    }
}
