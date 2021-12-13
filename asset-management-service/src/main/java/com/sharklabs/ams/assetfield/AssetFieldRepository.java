package com.sharklabs.ams.assetfield;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AssetFieldRepository extends JpaRepository<AssetField,Long> {
    Set<AssetField> findAllByAssetUUID(String uuid);
    List<AssetField> findAssetFieldsByAssetUUID(String uuid);

    @Query("SELECT DISTINCT (t.fieldId) FROM t_asset_field t WHERE t.assetUUID IN :uuids")
    List<String> findFieldIdByAssetUUIDIn(@Param("uuids") List<String> uuids);
}
