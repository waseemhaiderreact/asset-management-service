package com.sharklabs.ams.asset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset,Long> {
    Asset findAssetByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);

    Page<Asset> findByIdNotNull(Pageable pageable);

    List<Asset> findByUuidIn(List<String> uuids);
}
