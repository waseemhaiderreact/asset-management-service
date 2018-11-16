package com.sharklabs.ams.issuesreporting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;


@Entity(name = "t_issue_reporting")
public class IssueReporting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    private String issueDescription;

    @Lob
    private byte[] content;
    private String udid;
    private String status;

    public IssueReporting() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
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
}
