package com.sharklabs.ams.workorder;

import com.sharklabs.ams.vehicle.Vehicle;
import com.sharklabs.ams.workorderlineitems.WorkOrderLineItems;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "t_work_order")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String workOrderNumber;

    private String status;
    private Date completedAt;
    private String labels;
    private String description;
    private String odometer;
    private String secondaryMeter;
    private int discountPercentage;
    private String discountType;
    private int downtimeInSeconds;
    private String invoiceNumber;
    private String poNumber;
    private Date issuedAt;
    private Long issuedById;
    private String issuedByName;
    private String assignedToName;
    private Long contactId;
    private String contactName;
    private Date startedAt;
    private int tax1Percentage;
    private String tax1Type;
    private int tax2Percentage;
    private String tax2Type;
    private Float discount;
    private Float laborSubtotal;
    private Float partsSubtotal;
    private Float tax1;
    private Float tax2;
    private Float totalAmount;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "workOrder",fetch = FetchType.EAGER)
    private Set<WorkOrderLineItems> workOrderLineItems =new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public int getDowntimeInSeconds() {
        return downtimeInSeconds;
    }

    public void setDowntimeInSeconds(int downtimeInSeconds) {
        this.downtimeInSeconds = downtimeInSeconds;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getIssuedById() {
        return issuedById;
    }

    public void setIssuedById(Long issuedById) {
        this.issuedById = issuedById;
    }

    public String getIssuedByName() {
        return issuedByName;
    }

    public void setIssuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public int getTax1Percentage() {
        return tax1Percentage;
    }

    public void setTax1Percentage(int tax1Percentage) {
        this.tax1Percentage = tax1Percentage;
    }

    public String getTax1Type() {
        return tax1Type;
    }

    public void setTax1Type(String tax1Type) {
        this.tax1Type = tax1Type;
    }

    public int getTax2Percentage() {
        return tax2Percentage;
    }

    public void setTax2Percentage(int tax2Percentage) {
        this.tax2Percentage = tax2Percentage;
    }

    public String getTax2Type() {
        return tax2Type;
    }

    public void setTax2Type(String tax2Type) {
        this.tax2Type = tax2Type;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getLaborSubtotal() {
        return laborSubtotal;
    }

    public void setLaborSubtotal(Float laborSubtotal) {
        this.laborSubtotal = laborSubtotal;
    }

    public Float getPartsSubtotal() {
        return partsSubtotal;
    }

    public void setPartsSubtotal(Float partsSubtotal) {
        this.partsSubtotal = partsSubtotal;
    }

    public Float getTax1() {
        return tax1;
    }

    public void setTax1(Float tax1) {
        this.tax1 = tax1;
    }

    public Float getTax2() {
        return tax2;
    }

    public void setTax2(Float tax2) {
        this.tax2 = tax2;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<WorkOrderLineItems> getWorkOrderLineItems() {
        return workOrderLineItems;
    }

    public void setWorkOrderLineItems(Set<WorkOrderLineItems> workOrderLineItems) {
        this.workOrderLineItems = workOrderLineItems;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getSecondaryMeter() {
        return secondaryMeter;
    }

    public void setSecondaryMeter(String secondaryMeter) {
        this.secondaryMeter = secondaryMeter;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
}
