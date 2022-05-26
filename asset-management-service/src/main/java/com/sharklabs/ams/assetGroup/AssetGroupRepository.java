package com.sharklabs.ams.assetGroup;

import com.sharklabs.ams.asset.Asset;
import com.sharklabs.ams.minimalinfo.MinimalInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface AssetGroupRepository extends JpaRepository<AssetGroup, Long> {

    AssetGroup findAssetGroupByUuid(String uuid);

    List<AssetGroup> findAssetGroupByUuid(List<String> strings);

    @Transactional
    void deleteAssetGroupByUuid(String uuid);

    List<AssetGroup> findAssetGroupByAssetsIn(List<Asset> assets);

    Page<AssetGroup> findAssetGroupByTenantUUIDOrderByCreatedAtDesc(String tenantUUID, Pageable pageable);
    Page<AssetGroup> findAssetGroupByTenantUUIDOrderByGroupNameAsc(String tenantUUID, Pageable pageable);
    Set<AssetGroup> findAssetGroupByTenantUUIDOrderByGroupNameAsc(String tenantUUID);

//    List<MinimalInfo.AssetGroupInfo> findAssetGroupByTenantUUIDAndDeletefromGroupUUIDIsNull(String uuid);

    @Query("SELECT new com.sharklabs.ams.assetGroup.AssetGroupDTO(a.uuid,a.groupName) FROM t_asset_groups a WHERE a.tenantUUID=?1 AND a.deletefromGroupUUID is NULL")
    List<AssetGroupDTO> findAssetGroupByTenantUUIDAndDeletefromGroupUUIDIsNull(@Param("uuid") String uuid);

    @Query("SELECT distinct new com.sharklabs.ams.assetGroup.AssetGroupDTO(a.uuid,a.groupName) FROM t_asset_groups a JOIN a.assets ac WHERE ac.uuid in :assetUUIDs AND a.deletefromGroupUUID is NULL")
    List<AssetGroupDTO> findAssetGroupByAssetsInAndDeletefromGroupUUIDIsNull(@Param("assetUUIDs") Set<String> assetUUIDs);

    @Query("SELECT distinct new com.sharklabs.ams.assetGroup.AssetGroupDTO(a.uuid,a.groupName) FROM t_asset_groups a WHERE a.uuid in :assetUUIDs AND a.deletefromGroupUUID is NULL")
    List<AssetGroupDTO> findAssetGroupByUuidInAndDeletefromGroupUUIDIsNull(@Param("assetUUIDs") Set<String> assetUUIDs);
    @Query("SELECT distinct new com.sharklabs.ams.assetGroup.AssetGroupDTO(a.uuid,a.groupName) FROM t_asset_groups a WHERE a.groupName like %:Query% AND a.deletefromGroupUUID is NULL")
    List<AssetGroupDTO> findAssetGroupByNameAndDeletefromGroupUUIDIsNull(@Param("Query") String Query);
}
