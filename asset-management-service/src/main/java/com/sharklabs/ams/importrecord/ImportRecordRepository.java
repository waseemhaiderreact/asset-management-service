package com.sharklabs.ams.importrecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportRecordRepository extends JpaRepository<ImportRecord,Long> {

    List<ImportRecord> findImportRecordsByImportUUIDAndStatus(String uuid,String status);
}
