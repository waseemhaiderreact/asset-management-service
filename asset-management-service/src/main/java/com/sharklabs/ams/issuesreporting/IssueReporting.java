package com.sharklabs.ams.issuesreporting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.imagevoice.ImageVoice;
import com.sharklabs.ams.inspectionreportfield.InspectionReportField;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "t_issue_reporting")
public class IssueReporting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String issueNumber;

    private String issueDescription;
    private String udid;
    private String status;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "issue",fetch = FetchType.EAGER)
    private Set<ImageVoice> imageVoices =new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inspection_report_field_id")
    private InspectionReportField inspectionReportField;

    public IssueReporting() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<ImageVoice> getImageVoices() {
        return imageVoices;
    }

    public void setImageVoices(Set<ImageVoice> imageVoices) {
        this.imageVoices = imageVoices;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public InspectionReportField getInspectionReportField() {
        return inspectionReportField;
    }

    public void setInspectionReportField(InspectionReportField inspectionReportField) {
        this.inspectionReportField = inspectionReportField;
    }
}
