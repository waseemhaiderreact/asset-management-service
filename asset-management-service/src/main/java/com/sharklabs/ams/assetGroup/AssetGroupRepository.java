package com.sharklabs.ams.assetGroup;

import com.sharklabs.ams.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface AssetGroupRepository extends JpaRepository<AssetGroup, Long> {

    AssetGroup findAssetGroupByUuid(String uuid);

    @Transactional
    void deleteAssetGroupByUuid(String uuid);

    List<AssetGroup> findAssetGroupByAssetsIn(List<Asset> assets);

    Page<AssetGroup> findAssetGroupByTenantUUIDOrderByCreatedAtDesc(String tenantUUID, Pageable pageable);
    Page<AssetGroup> findAssetGroupByTenantUUIDOrderByGroupNameAsc(String tenantUUID, Pageable pageable);
    Set<AssetGroup> findAssetGroupByTenantUUIDOrderByGroupNameAsc(String tenantUUID);

}
