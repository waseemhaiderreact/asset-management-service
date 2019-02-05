package com.sharklabs.ams.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface AssetRepository extends JpaRepository<Asset,Long> {
    Asset findAssetByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);
}