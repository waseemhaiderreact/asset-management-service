package com.sharklabs.ams.assetimport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetImportRepository extends JpaRepository<AssetImport,Long> {

    AssetImport findByUuid(String uuid);
}
