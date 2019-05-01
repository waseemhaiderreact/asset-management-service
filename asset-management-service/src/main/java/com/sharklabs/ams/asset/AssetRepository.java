package com.sharklabs.ams.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface AssetRepository extends JpaRepository<Asset,Long>, PagingAndSortingRepository<Asset,Long> {
    Asset findAssetByUuid(String uuid);
    List<Asset> findByTenantUUID(String tenantUUID);

    @Modifying
    @Transactional
    Integer deleteById(Long id);


//    List<Asset> findByUuidIn(List<String> uuids);

//    @Query(value = "SELECT new com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse(a.name, a.name, a.assetNumber, a.uuid) " +
//            "FROM Asset a " +
//            "WHERE a.uuid in :uuids ")
//    List<GetNameAndTypeOfAssetResponse> findByUuidIn(@Param("uuids") List<String> uuids);
}
