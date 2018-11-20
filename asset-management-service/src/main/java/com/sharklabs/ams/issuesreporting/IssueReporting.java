package com.sharklabs.ams.issuesreporting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.Recording.Recording;
import com.sharklabs.ams.image.Image;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private String udid;
    private String status;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "issue",fetch = FetchType.EAGER)
    private Set<Image> images =new HashSet<>();

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "issue",fetch = FetchType.EAGER)
    private Set<Recording> recordings =new HashSet<>();

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

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(Set<Recording> recordings) {
        this.recordings = recordings;
    }
}
