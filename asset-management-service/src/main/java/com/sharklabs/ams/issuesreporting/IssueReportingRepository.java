package com.sharklabs.ams.issuesreporting;

import com.sharklabs.ams.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueReportingRepository extends JpaRepository<IssueReporting,Long> {
    IssueReporting findById(Long id);
    Iterable<IssueReporting> findAllByVehicle_AssetNumber(String assetNumber);
    IssueReporting findByIssueNumber(String issueNumber);
    Page<IssueReporting> findByIdNotNull(Pageable page);
}
