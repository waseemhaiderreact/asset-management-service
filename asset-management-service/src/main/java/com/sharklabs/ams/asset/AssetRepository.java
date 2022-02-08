package com.sharklabs.ams.asset;

import com.sharklabs.ams.assetfield.AssetField;
import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModel;
import com.sharklabs.ams.minimalinfo.MinimalInfo;
import com.sharklabs.ams.model.AssetAndCategoryUUIDModel;
import com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public interface AssetRepository extends JpaRepository<Asset,Long>, PagingAndSortingRepository<Asset,Long> {

    Set<Asset> findAssetsByUuidIn(List<String> assetUuids);

    Set<Asset> findAssetsByUuidInOrderByIdDesc(List<String> assetUuids);

    @Query("SELECT a FROM t_asset a WHERE a.uuid = ?1")
    Asset findAssetByUuid(String uuid);

    @Query("SELECT new com.sharklabs.ams.asset.AssetInfoDTO(a.uuid,a.name,a.assetNumber,c.name,c.uuid) FROM t_asset a, t_category c WHERE a.uuid= ?1 AND c.uuid = a.categoryUUID")
    AssetInfoDTO findAssetInfoByAssetUUID(@Param("uuid") String uuid);

    List<Asset> findByTenantUUID(String tenantUUID);

    @Query("SELECT a.status FROM t_asset a WHERE a.uuid = ?1")
    String getAssetStatusByUUID(@Param("uuid") String uuid);

    @Query("SELECT a.categoryUUID FROM t_asset a WHERE a.uuid = ?1")
    String getAssetCategoryUUIDByUUID(@Param("uuid") String uuid);

    @Query(value = "SELECT uuid,name FROM t_asset WHERE tenantuuid LIKE :tenantUUID",nativeQuery = true)
    List<Object> getAssetNameAndUUIDByTenantUUID(@Param("tenantUUID") String tenantUUID);

    @Query("SELECT a FROM t_asset a WHERE a.uuid = ?1")
    Asset findByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteByUuid(String uuid);

    boolean existsByModelNumber(String modelNumber);

    // Basic Asset Detail i.e w/o Collections and ActivityWall
    @Query("SELECT new com.sharklabs.ams.asset.AssetDetail(a.id, a.assetNumber, a.uuid, a.name, a.modelNumber, a.manufacture, a.purchaseDate, a.expiryDate, a.warranty, a.description, a.tenantUUID, a.primaryUsageUnit, a.secondaryUsageUnit, a.consumptionUnit, a.consumptionPoints, a.status,a.categoryUUID) FROM t_asset a WHERE a.uuid = ?1")
    AssetDetail getBasicAssetDetailByUuid(String uuid);


//    List<Asset> findByUuidIn(List<String> uuids);

//    @Query(value = "SELECT new com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse(a.name, a.name, a.assetNumber, a.uuid) " +
//            "FROM Asset a " +
//            "WHERE a.uuid in :uuids ")
//    List<GetNameAndTypeOfAssetResponse> findByUuidIn(@Param("uuids") List<String> uuids);

    @Query(value = "SELECT uuid FROM t_asset WHERE name LIKE %:assetName%", nativeQuery = true)
    List<Object> findAssetUUIDByAssetName(@Param("assetName") String assetName);

    @Query(value = "SELECT * FROM t_asset t WHERE t.uuid LIKE :assetUUID",nativeQuery = true)
    Asset findAsset(@Param("assetUUID")String assetUUID);

    ArrayList<Asset> findAssetsByTenantUUID(String tenantUUID);

    Asset findByAssetNumber(String AssetNumber);

//    List<MinimalInfo.AssetInfo> findAssetByTenantUUIDAndRemoveFromCategoryUUIDIsNull(String uuid);

    @Query("SELECT new com.sharklabs.ams.asset.AssetDTO(a.uuid,a.name) FROM t_asset a WHERE a.tenantUUID=?1 AND a.removeFromCategoryUUID " +
            "is NULL ")
    List<AssetDTO> findAssetByTenantUUIDAndRemoveFromCategoryUUIDIsNull(@Param("uuid") String uuid);

    @Query("SELECT c.name FROM t_asset a, t_category c WHERE a.uuid=?1 AND c.uuid=a.categoryUUID")
    String findCategoryNameByAssetUUID(@Param("uuid") String uuid);

    @Query(value = "SELECT new com.sharklabs.ams.response.GetNameAndTypeOfAssetResponse(a.name,a.categoryUUID,a.assetNumber,a.uuid,a.primaryUsageUnit,a.secondaryUsageUnit,a.consumptionUnit,a.consumptionPoints,a.modelNumber,c.name) FROM t_asset a,t_category c WHERE a.uuid in (:uuids) AND c.uuid = a.categoryUUID")
    List<GetNameAndTypeOfAssetResponse> findAssetDetailByAssetUUIDS(@Param("uuids") List<String> uuids);

    @Query("SELECT  new com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModel(a.uuid,a.name,c.name,a.assetNumber,c.uuid) FROM t_asset a, t_category c WHERE a.tenantUUID=?1 AND c.uuid=a.categoryUUID")
    List<AssetBasicDetailModel> findAssetBasicDetailByTenantUUID(@Param("tenantUUID") String tenantUUID);

    @Query("SELECT a FROM t_asset a WHERE a.assetNumber IN (:assetNumber)")
    List<Asset> findAssetsByAssetNumberIn(@Param("assetNumber") List<String> assetNumber);

    @Query("SELECT new com.sharklabs.ams.asset.AssetDTO(a.uuid,a.name) FROM t_asset a WHERE a.uuid in :uuid AND a.removeFromCategoryUUID " +
            "is NULL ")
    List<AssetDTO> findAssetByUuidAndRemoveFromCategoryUUIDIsNull(@Param("uuid") Set<String> uuid);

    @Query("SELECT new com.sharklabs.ams.asset.AssetMapper(a.uuid,a.assetNumber,a.name,c.name,a.status,a.archive,a.removeFromCategoryUUID,a.tenantUUID) FROM t_asset a, t_category c WHERE a.tenantUUID=?1 AND c.uuid=a.categoryUUID")
    List<AssetMapper> findAssetsInfoByTenantUUID(@Param("tenantUUID") String tenantUUID);

    @Query("SELECT new com.sharklabs.ams.asset.AssetMapper(a.uuid,a.assetNumber,a.name,c.name,a.status,a.archive,a.removeFromCategoryUUID,a.tenantUUID) FROM t_asset a, t_category c WHERE a.uuid NOT in :uuids AND a.tenantUUID=:tenantUUID AND c.uuid= a.categoryUUID")
    List<AssetMapper> findAssetsByUuidNotIn(@Param("uuids") List<String> uuids, @Param("tenantUUID") String tenantUUID);

    //query to get uuids for Assignee mapping
    @Query(value = "SELECT a.uuid from t_asset a where a.tenantUUID=?1 AND a.uuid is not null")
    List<String> findAssetsUuidsByTenantUUID(@Param("tenantUUID") String tenantUUID);
    @Query("SELECT new com.sharklabs.ams.asset.AssetDTO(a.uuid,a.name) FROM t_asset a WHERE a.name like %:Query% AND a.removeFromCategoryUUID " +
            "is NULL ")
    List<AssetDTO> findAssetByNameAndRemoveFromCategoryUUIDIsNull(@Param("Query") String Query);

    @Query("SELECT new com.sharklabs.ams.model.AssetAndCategoryUUIDModel(a.uuid,a.name,c.name,c.uuid) FROM t_asset a, t_category c WHERE a.uuid in (:uuids) and c.uuid=a.categoryUUID")
    List<AssetAndCategoryUUIDModel> findAssetNameAndCategoryNameByUUIDS(@Param("uuids") List<String> uuids);
}
