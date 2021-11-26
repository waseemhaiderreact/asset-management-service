package com.sharklabs.ams.importtemplate;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_import_template")
public class ImportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    @Length(max = 999999)
    private String categoryUUID;

    @Length(max = 999999)
    private String categoryName;

    private String userUUID;

    private String userName;

    private String tenantUUID;

    @Lob
    private byte[] csvColumnData;

    private String templateName;

    private String templateNumber;

    private Date createdDate;

    private String importType;

    private String importedData;

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

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserName() {
        return userName;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getImportedData() {
        return importedData;
    }

    public void setImportedData(String importedData) {
        this.importedData = importedData;
    }
}
