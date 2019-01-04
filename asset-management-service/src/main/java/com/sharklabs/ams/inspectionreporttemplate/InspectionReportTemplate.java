package com.sharklabs.ams.inspectionreporttemplate;

import com.sharklabs.ams.inspectionreporttemplatefield.InspectionReportTemplateField;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_inspection_report_template")
public class InspectionReportTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private String createdBy;

    private Date createdAt;

    private String status;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "inspectionReportTemplate",fetch = FetchType.EAGER)
    private Set<InspectionReportTemplateField> inspectionReportTemplateFields=new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Set<InspectionReportTemplateField> getInspectionReportTemplateFields() {
        return inspectionReportTemplateFields;
    }

    public void setInspectionReportTemplateFields(Set<InspectionReportTemplateField> inspectionReportTemplateFields) {
        this.inspectionReportTemplateFields = inspectionReportTemplateFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
