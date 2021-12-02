package com.sharklabs.ams.response;

import com.sharklabs.ams.assetimport.AssetImport;
import com.sharklabs.ams.importrecord.ImportRecord;

import java.util.List;

public class ImportBulkAssetResponse {

    private String responseIdentifier;

    private String description;

    private List<ImportRecord> importRecords;

    private AssetImport assetImport;

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImportRecord> getImportRecords() {
        return importRecords;
    }

    public void setImportRecords(List<ImportRecord> importRecords) {
        this.importRecords = importRecords;
    }

    public AssetImport getAssetImport() {
        return assetImport;
    }

    public void setAssetImport(AssetImport assetImport) {
        this.assetImport = assetImport;
    }
}
