package com.sharklabs.ams.meterentry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharklabs.ams.serviceentry.ServiceEntry;
import com.sharklabs.ams.vehicle.Vehicle;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "t_meter_entries")
public class MeterEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String meterType;

    private Long value;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "service_entry_id", nullable = false)
    private ServiceEntry serviceEntry;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ServiceEntry getServiceEntry() {
        return serviceEntry;
    }

    public void setServiceEntry(ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }
}
