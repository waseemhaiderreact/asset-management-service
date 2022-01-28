package com.sharklabs.ams.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetMapperRepository extends JpaRepository<AssetMapper, Long> {

    @Query(value = "SELECT a.uuid FROM AssetMapper a where a.tenantUUID=?1")
    List<String> findByTenantUUID(String tenantUUID);

    AssetMapper findByUuid(String uuid);

    @Query(value = "SELECT DISTINCT (a) FROM AssetMapper a where a.uuid in :uuids")
    List<AssetMapper> findAllByUuid(@Param("uuids") List<String> uuids);

    List<AssetMapper> findAllByTenantUUID(String tenantUUID);
}
