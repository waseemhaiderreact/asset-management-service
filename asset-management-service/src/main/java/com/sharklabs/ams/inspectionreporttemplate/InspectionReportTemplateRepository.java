package com.sharklabs.ams.inspectionreporttemplate;

import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionReportTemplateRepository extends JpaRepository<InspectionReportTemplate,Long> {
    Iterable<InspectionReportTemplate> findByVehicle(Vehicle vehicle);
    Page<InspectionReportTemplate> findByIdNotNull(Pageable page);
}
