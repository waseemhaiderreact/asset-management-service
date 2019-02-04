package com.sharklabs.ams.inspectiontemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface InspectionTemplateRepository extends JpaRepository<InspectionTemplate,Long> {
    InspectionTemplate findInspectionTemplateByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);
}
