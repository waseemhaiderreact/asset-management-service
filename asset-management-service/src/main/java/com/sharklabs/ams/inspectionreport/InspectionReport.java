package com.sharklabs.ams.inspectionreport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.inspectionreportfield.InspectionReportField;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;
import java.util.*;

@Entity(name = "t_inspection_report")
public class InspectionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reportedBy;

    private Date createdAt;

    private String status;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "inspectionReport",fetch = FetchType.EAGER)
    private Set<InspectionReportField> inspectionReportFields=new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<InspectionReportField> getInspectionReportFields() {
        return inspectionReportFields;
    }

    public void setInspectionReportFields(Set<InspectionReportField> inspectionReportFields) {
        this.inspectionReportFields = inspectionReportFields;
    }
}
