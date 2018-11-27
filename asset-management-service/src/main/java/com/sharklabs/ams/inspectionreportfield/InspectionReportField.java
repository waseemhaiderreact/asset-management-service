package com.sharklabs.ams.inspectionreportfield;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.inspectionreport.InspectionReport;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;

@Entity(name = "t_inspection_report_field")
public class InspectionReportField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fieldName;

    private boolean fieldStatus;

    private String fieldValue;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection_report_id",referencedColumnName = "id")
    private InspectionReport inspectionReport;

    @OneToOne(mappedBy = "inspectionReportField", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private IssueReporting issueReporting;

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

    public boolean isFieldStatus() {
        return fieldStatus;
    }

    public void setFieldStatus(boolean fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    public InspectionReport getInspectionReport() {
        return inspectionReport;
    }

    public void setInspectionReport(InspectionReport inspectionReport) {
        this.inspectionReport = inspectionReport;
    }

    public IssueReporting getIssueReporting() {
        return issueReporting;
    }

    public void setIssueReporting(IssueReporting issueReporting) {
        this.issueReporting = issueReporting;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
