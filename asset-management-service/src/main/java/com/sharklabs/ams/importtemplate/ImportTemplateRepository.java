package com.sharklabs.ams.importtemplate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportTemplateRepository extends JpaRepository<ImportTemplate,Long> {

    List<ImportTemplate> findByTenantUUIDAndUserUUID(String tenantuuid, String useruuid);
}
