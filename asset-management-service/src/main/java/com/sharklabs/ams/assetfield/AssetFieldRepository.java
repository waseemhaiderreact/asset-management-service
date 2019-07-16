package com.sharklabs.ams.assetfield;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AssetFieldRepository extends JpaRepository<AssetField,Long> {
    Set<AssetField> findAllByAssetUuid(String uuid);
}
