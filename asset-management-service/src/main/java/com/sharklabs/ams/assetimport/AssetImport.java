package com.sharklabs.ams.assetimport;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_asset_import")
public class AssetImport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    private String importName;

    private Date importDate;

    private String percentageComplete;

    private String message;// range of records complete

    private String status;// success/fail/partial fail

    @Lob
    private byte[] columns;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(String percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getColumns() {
        return columns;
    }

    public void setColumns(byte[] columns) {
        this.columns = columns;
    }
}
