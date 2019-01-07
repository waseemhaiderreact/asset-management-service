package com.sharklabs.ams.inspectionreporttemplatefield;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplate;

import javax.persistence.*;

@Entity(name="t_inspection_report_template_field")
public class InspectionReportTemplateField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fieldName;

    private String fieldType;

    private String question;

    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection_report_template_id",referencedColumnName = "id")
    private InspectionReportTemplate inspectionReportTemplate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public InspectionReportTemplate getInspectionReportTemplate() {
        return inspectionReportTemplate;
    }

    public void setInspectionReportTemplate(InspectionReportTemplate inspectionReportTemplate) {
        this.inspectionReportTemplate = inspectionReportTemplate;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
