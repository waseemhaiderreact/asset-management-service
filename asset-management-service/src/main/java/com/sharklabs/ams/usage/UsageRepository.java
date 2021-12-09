package com.sharklabs.ams.usage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface  UsageRepository extends JpaRepository<Usage,Long> {

    Set<Usage> findByAssetUUID(String uuid);
    Set<Usage> findByAssetUUIDOrderByIdDesc(String uuid);
    Usage findFirstByAssetUUIDOrderByIdDesc(String uuid);

    

    Page<Usage> findByAssetUUIDOrderByCreatedAtDesc(String assetUUID, Pageable pageable);

    @Query(value = "SELECT * FROM t_usages u "+
            "WHERE ((:assetUUID is null) or (u.assetuuid=:assetUUID)) "+
            "AND ((:tenantUUID is null) or (u.tenantuuid=:tenantUUID)) "+
            "AND ((:category is null) or (u.category=:category)) "+
            "AND ((:startDate is null) or (u.created_at BETWEEN :startDate AND :endDate)) \n#pageable\n",
            countQuery = "SELECT count(*) FROM t_usages u "+
                    "WHERE ((:assetUUID is null) or (u.assetuuid=:assetUUID)) "+
                    "AND ((:tenantUUID is null) or (u.tenantuuid=:tenantUUID)) "+
                    "AND ((:category is null) or (u.category=:category)) "+
                    "AND ((:startDate is null) or (u.created_at BETWEEN :startDate AND :endDate)) \n#pageable\n",nativeQuery = true)
    Page<Usage> filterUsages(@Param("assetUUID") String assetUUID,
                             @Param("tenantUUID") String tenantUUID,
                             @Param("category") String category,
                             @Param("startDate") Date startDate,
                             @Param("endDate") Date endDate,
                             Pageable pageable);

    Page<Usage> findByAssetUUIDInOrderByIdDesc(List<String> assetUUIDS, Pageable pageable);
    
    Page<Usage> findByAssetUUIDInAndCategoryOrderByIdDesc(List<String> assetUUIDS,String category, Pageable pageable);
}
