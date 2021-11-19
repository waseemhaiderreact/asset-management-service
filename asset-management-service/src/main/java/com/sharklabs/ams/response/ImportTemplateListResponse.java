package com.sharklabs.ams.response;

import com.sharklabs.ams.importtemplate.ImportTemplateDTO;

import java.util.List;

public class ImportTemplateListResponse {

    private List<ImportTemplateDTO> importTemplates;

    private String responseIdentifier;

    public List<ImportTemplateDTO> getImportTemplates() {
        return importTemplates;
    }

    public void setImportTemplates(List<ImportTemplateDTO> importTemplates) {
        this.importTemplates = importTemplates;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }
}
