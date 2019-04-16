package com.sharklabs.ams.asset;

import com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset,Long> {
    Asset findAssetByUuid(String uuid);

    List<Asset> findByTenantUUID(String tenantUUID);

    @Modifying
    @Transactional
    Integer deleteById(Long id);

    Page<Asset> findByIdNotNull(Pageable pageable);

//    List<Asset> findByUuidIn(List<String> uuids);

//    @Query(value = "SELECT new com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse(a.name, a.name, a.assetNumber, a.uuid) " +
//            "FROM Asset a " +
//            "WHERE a.uuid in :uuids ")
//    List<GetNameAndTypeOfAssetResponse> findByUuidIn(@Param("uuids") List<String> uuids);
}
