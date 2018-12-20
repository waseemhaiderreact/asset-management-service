package com.sharklabs.ams.vehicle;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.inspectionreport.InspectionReport;
import com.sharklabs.ams.inspectionreporttemplate.InspectionReportTemplate;
import com.sharklabs.ams.issuesreporting.IssueReporting;
import com.sharklabs.ams.meterentry.MeterEntry;
import com.sharklabs.ams.serviceentry.ServiceEntry;

import javax.persistence.*;
import java.util.*;

@Entity(name = "t_vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String assetNumber;
    private String color;
    private String type;
    private String fuelVolumeUnits;
    private String licensePlate;
    private String loanAccountNumber;
    private String loanInterestRate;
    private String loanNotes;
    private Date loanStartedAt;
    private Date loanEndedAt;
    private String make;
    private String meterUnit;
    private String model;
    private String name;
    private String ownership;
    private int registrationExpirationMonth;
    private String registrationState;
    private boolean secondaryMeter;
    private String secondaryMeterUnit;
    private String systemOfMeasurement;
    private String trim;
    private String vin;
    private String year;
    private double residualValue;
    private double loanAmount;
    private double loanPayment;
    private Date purchaseDate;
    private double price;
    private Date createdAt;
    private Date updatedAt;

    @Lob
    private byte[] image;

    @JsonIgnore
    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "vehicle",fetch = FetchType.EAGER)
    private Set<InspectionReport> inspectionReports =new HashSet<>();

    @JsonIgnore
    @OneToMany(  cascade = CascadeType.ALL,mappedBy = "vehicle",fetch = FetchType.EAGER)
    private Set<InspectionReportTemplate> inspectionReportTemplates =new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},mappedBy = "vehicle",fetch = FetchType.EAGER)
    private Set<IssueReporting> issueReportings =new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE}, mappedBy = "vehicle",fetch = FetchType.EAGER)
    private Set<ServiceEntry> serviceEntries =new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE},mappedBy = "vehicle",fetch = FetchType.EAGER)
    private Set<MeterEntry> meterEntries =new HashSet<>();

    public  void addInspectionReport(InspectionReport inspectionReport){
        this.inspectionReports.add(inspectionReport);
    }

    public void addInspectionReportTemplate(InspectionReportTemplate inspectionReportTemplate){
        this.inspectionReportTemplates.add(inspectionReportTemplate);
    }


    public void addIssueReporting(IssueReporting issueReporting){
        this.issueReportings.add(issueReporting);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuelVolumeUnits() {
        return fuelVolumeUnits;
    }

    public void setFuelVolumeUnits(String fuelVolumeUnits) {
        this.fuelVolumeUnits = fuelVolumeUnits;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getLoanInterestRate() {
        return loanInterestRate;
    }

    public void setLoanInterestRate(String loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }

    public String getLoanNotes() {
        return loanNotes;
    }

    public void setLoanNotes(String loanNotes) {
        this.loanNotes = loanNotes;
    }

    public Date getLoanStartedAt() {
        return loanStartedAt;
    }

    public void setLoanStartedAt(Date loanStartedAt) {
        this.loanStartedAt = loanStartedAt;
    }

    public Date getLoanEndedAt() {
        return loanEndedAt;
    }

    public void setLoanEndedAt(Date loanEndedAt) {
        this.loanEndedAt = loanEndedAt;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getMeterUnit() {
        return meterUnit;
    }

    public void setMeterUnit(String meterUnit) {
        this.meterUnit = meterUnit;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public int getRegistrationExpirationMonth() {
        return registrationExpirationMonth;
    }

    public void setRegistrationExpirationMonth(int registrationExpirationMonth) {
        this.registrationExpirationMonth = registrationExpirationMonth;
    }

    public String getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    public boolean isSecondaryMeter() {
        return secondaryMeter;
    }

    public void setSecondaryMeter(boolean secondaryMeter) {
        this.secondaryMeter = secondaryMeter;
    }

    public String getSecondaryMeterUnit() {
        return secondaryMeterUnit;
    }

    public void setSecondaryMeterUnit(String secondaryMeterUnit) {
        this.secondaryMeterUnit = secondaryMeterUnit;
    }

    public String getSystemOfMeasurement() {
        return systemOfMeasurement;
    }

    public void setSystemOfMeasurement(String systemOfMeasurement) {
        this.systemOfMeasurement = systemOfMeasurement;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(double residualValue) {
        this.residualValue = residualValue;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getLoanPayment() {
        return loanPayment;
    }

    public void setLoanPayment(double loanPayment) {
        this.loanPayment = loanPayment;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<InspectionReport> getInspectionReports() {
        return inspectionReports;
    }

    public void setInspectionReports(Set<InspectionReport> inspectionReports) {
        this.inspectionReports = inspectionReports;
    }

    public Set<InspectionReportTemplate> getInspectionReportTemplates() {
        return inspectionReportTemplates;
    }

    public void setInspectionReportTemplates(Set<InspectionReportTemplate> inspectionReportTemplates) {
        this.inspectionReportTemplates = inspectionReportTemplates;
    }

    public Set<IssueReporting> getIssueReportings() {
        return issueReportings;
    }

    public void setIssueReportings(Set<IssueReporting> issueReportings) {
        this.issueReportings = issueReportings;
    }

    public Set<ServiceEntry> getServiceEntries() {
        return serviceEntries;
    }

    public void setServiceEntries(Set<ServiceEntry> serviceEntries) {
        this.serviceEntries = serviceEntries;
    }

    public Set<MeterEntry> getMeterEntries() {
        return meterEntries;
    }

    public void setMeterEntries(Set<MeterEntry> meterEntries) {
        this.meterEntries = meterEntries;
    }
}
