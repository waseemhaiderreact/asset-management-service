package com.sharklabs.ams.AssignmentRecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRecordRepository extends JpaRepository<AssignmentRecord,Long>{
    AssignmentRecord findByAssetNumberAndDriverNumber(String assetNumber,String driverNumber);
}
