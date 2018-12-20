package com.sharklabs.ams.serviceentry;

import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.meterentry.MeterEntry;
import com.sharklabs.ams.servicetask.ServiceTask;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_service_entries")
public class ServiceEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date completedAt;

    private String vendorId;

    private int taxPercentage;

    private Long laborSubtotal;

    private Long partsSubtotal;

    private Long totalAmount;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

//    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.PERSIST},mappedBy = "serviceEntries",fetch = FetchType.EAGER)
//    private Set<ServiceTask> serviceTasks=new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE}, mappedBy = "serviceEntries")
    private Set<ServiceTask> serviceTasks = new HashSet<>();


    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},mappedBy = "serviceEntry",fetch = FetchType.EAGER)
    private Set<IssueReporting> issueReportings =new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL,
            mappedBy = "serviceEntry")
    private MeterEntry meterEntry;

    private Date createdAt;

    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(int taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Long getLaborSubtotal() {
        return laborSubtotal;
    }

    public void setLaborSubtotal(Long laborSubtotal) {
        this.laborSubtotal = laborSubtotal;
    }

    public Long getPartsSubtotal() {
        return partsSubtotal;
    }

    public void setPartsSubtotal(Long partsSubtotal) {
        this.partsSubtotal = partsSubtotal;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<ServiceTask> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(Set<ServiceTask> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }

    public Set<IssueReporting> getIssueReportings() {
        return issueReportings;
    }

    public void setIssueReportings(Set<IssueReporting> issueReportings) {
        this.issueReportings = issueReportings;
    }

    public MeterEntry getMeterEntry() {
        return meterEntry;
    }

    public void setMeterEntry(MeterEntry meterEntry) {
        this.meterEntry = meterEntry;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
