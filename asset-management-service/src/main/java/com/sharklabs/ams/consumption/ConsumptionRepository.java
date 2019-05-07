package com.sharklabs.ams.consumption;

import com.sharklabs.ams.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;

public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {
    Consumption findConsumptionByUuid(String uuid);


    @Modifying
    @Transactional
    Integer deleteById(Long id);

    Page<Consumption> findByAssetOrderByCreatedAt(Asset asset, Pageable pageable);
}
