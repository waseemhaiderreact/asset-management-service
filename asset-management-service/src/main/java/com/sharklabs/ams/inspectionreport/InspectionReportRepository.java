package com.sharklabs.ams.inspectionreport;

import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspectionReportRepository extends JpaRepository<InspectionReport,Long> {
    Iterable<InspectionReport> findByVehicle(Vehicle vehicle);
}
