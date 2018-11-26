package com.sharklabs.ams.issuesreporting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueReportingRepository extends JpaRepository<IssueReporting,Long> {
    IssueReporting findById(Long id);
}
