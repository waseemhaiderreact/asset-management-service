package com.sharklabs.ams.consumption;

import com.sharklabs.ams.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {
    Consumption findConsumptionByUuid(String uuid);

    Set<Consumption> findByAssetUUID(String uuid);


    @Modifying
    @Transactional
    Integer deleteByUuid(String uuid);

    Page<Consumption> findByAssetUUIDOrderByCreatedAt(String assetUUID, Pageable pageable);

    @Query(value = "SELECT * FROM t_consumption c "+
            "WHERE ((:assetUUID is null) or (c.assetuuid=:assetUUID)) "+
            "AND ((:tenantUUID is null) or (c.tenantuuid=:tenantUUID)) "+
            "AND ((:startDate is null) or (c.created_at BETWEEN :startDate AND :endDate)) ORDER BY c.created_at DESC \n#pageable\n",
            countQuery = "SELECT count(*) FROM t_consumption c "+
                    "WHERE ((:assetUUID is null) or (c.assetuuid=:assetUUID)) "+
                    "AND ((:tenantUUID is null) or (c.tenantuuid=:tenantUUID)) "+
                    "AND ((:startDate is null && :endDate is null) or (c.created_at BETWEEN :startDate AND :endDate)) \n#pageable\n",nativeQuery = true)
    Page<Consumption> filterConsumptions(@Param("assetUUID") String assetUUID,
                             @Param("tenantUUID") String tenantUUID,
                             @Param("startDate") Date startDate,
                             @Param("endDate") Date endDate,
                             Pageable pageable);

    Page<Consumption> findByAssetUUIDInOrderByIdDesc(List<String> assetUUIDS, Pageable pageable);
}
