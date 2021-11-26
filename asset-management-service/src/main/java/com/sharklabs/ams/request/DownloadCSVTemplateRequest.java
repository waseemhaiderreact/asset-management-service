package com.sharklabs.ams.request;

public class DownloadCSVTemplateRequest {

    private String columnsData;

    private String templateName;

    public String getColumnsData() {
        return columnsData;
    }

    public void setColumnsData(String columnsData) {
        this.columnsData = columnsData;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
