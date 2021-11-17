package com.sharklabs.ams.response;

import com.sharklabs.ams.importtemplate.ImportTemplate;

import java.util.List;

public class ImportTemplateListResponse {

    private List<ImportTemplate> importTemplates;

    private String responseIdentifier;

    public List<ImportTemplate> getImportTemplates() {
        return importTemplates;
    }

    public void setImportTemplates(List<ImportTemplate> importTemplates) {
        this.importTemplates = importTemplates;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
