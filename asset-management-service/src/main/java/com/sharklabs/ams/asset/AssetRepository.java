package com.sharklabs.ams.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface AssetRepository extends JpaRepository<Asset,Long>, PagingAndSortingRepository<Asset,Long> {


    @Query("SELECT a FROM t_asset a WHERE a.uuid = ?1")
    Asset findAssetByUuid(String uuid);
    List<Asset> findByTenantUUID(String tenantUUID);

    @Query("SELECT a FROM t_asset a WHERE a.uuid = ?1")
    Asset findByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);



    // Basic Asset Detail i.e w/o Collections and ActivityWall
    @Query("SELECT new com.sharklabs.ams.asset.AssetDetail(a.id, a.assetNumber, a.uuid, a.name, a.modelNumber, a.inventory, a.manufacture, a.purchaseDate, a.expiryDate, a.warranty, a.description, a.tenantUUID, a.primaryUsageUnit, a.secondaryUsageUnit, a.consumptionUnit, a.consumptionPoints) FROM t_asset a WHERE a.uuid = ?1")
    AssetDetail getBasicAssetDetailByUuid(String uuid);





//    List<Asset> findByUuidIn(List<String> uuids);

//    @Query(value = "SELECT new com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse(a.name, a.name, a.assetNumber, a.uuid) " +
//            "FROM Asset a " +
//            "WHERE a.uuid in :uuids ")
//    List<GetNameAndTypeOfAssetResponse> findByUuidIn(@Param("uuids") List<String> uuids);
}
