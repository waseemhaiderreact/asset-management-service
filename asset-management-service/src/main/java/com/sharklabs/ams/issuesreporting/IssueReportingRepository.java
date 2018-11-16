package com.sharklabs.ams.issuesreporting;

import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueReportingRepository extends JpaRepository<IssueReporting,Long> {
    List<IssueReporting> findByVehicle(Vehicle vehicle);
    IssueReporting findById(Long id);
}
