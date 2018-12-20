package com.sharklabs.ams.issuesreporting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.imagevoice.ImageVoice;
import com.sharklabs.ams.inspectionreportfield.InspectionReportField;
import com.sharklabs.ams.serviceentry.ServiceEntry;
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
    private String status;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "issue",fetch = FetchType.EAGER)
    private Set<ImageVoice> imageVoices =new HashSet<>();

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinColumn(name = "inspection_report_field_id")
    private InspectionReportField inspectionReportField;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinColumn(name = "service_entry_id",referencedColumnName = "id")
    private ServiceEntry serviceEntry;


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

    public ServiceEntry getServiceEntry() {
        return serviceEntry;
    }

    public void setServiceEntry(ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

}
