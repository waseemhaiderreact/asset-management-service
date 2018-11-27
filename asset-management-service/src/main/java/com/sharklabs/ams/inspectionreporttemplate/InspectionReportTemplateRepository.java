package com.sharklabs.ams.inspectionreporttemplate;

import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionReportTemplateRepository extends JpaRepository<InspectionReportTemplate,Long> {
    Iterable<InspectionReportTemplate> findByVehicle(Vehicle vehicle);
}
