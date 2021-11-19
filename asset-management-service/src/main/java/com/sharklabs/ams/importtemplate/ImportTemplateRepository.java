package com.sharklabs.ams.importtemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImportTemplateRepository extends JpaRepository<ImportTemplate,Long> {

    List<ImportTemplate> findByTenantUUIDAndUserUUID(String tenantuuid, String useruuid);

    @Query("SELECT new com.sharklabs.ams.importtemplate.ImportTemplateDTO(it.uuid,it.csvColumnData,it.templateName,it.templateNumber,it.categoryUUID,it.categoryName) FROM ImportTemplate as it WHERE it.tenantUUID=?1 AND it.userUUID=?2")
    List<ImportTemplateDTO> findListByTenantUUIDAndUserUUID(String tenantUUID, String userUUID);
}
//it.uuid,it.csvColumnData,it.templateName,it.templateNumber,it.categoryUUID,it.categoryName)