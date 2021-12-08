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
//    @Query(value = "SELECT u from t_usages u where (u.primaryUsageValue)  = (" +
//            " SELECT CAST(MAX (uu.primaryUsageValue) AS INT) from t_usages uu where uu.assetUUID=:assetUUID) ")
//    Usage findUsageByAssetUUIDAndMaxPrimaryUsageValue(@Param("assetUUID") String assetUUID);
//
    @Query("SELECT DISTINCT u from t_usages u where cast(u.secondaryUsageValue as int)  = ( SELECT MAX(cast (uu.secondaryUsageValue as int)) from t_usages uu where uu.assetUUID=:assetUUID )  ")
    List<Usage> findUsageByAssetUUIDAndMaxSecondaryUsageValue(@Param("assetUUID") String assetUUID);

    @Query("SELECT distinct u from t_usages u where cast(u.primaryUsageValue as int)  = ( SELECT MAX(cast (uu.primaryUsageValue as int)) from t_usages uu where uu.assetUUID=:assetUUID )  " )
    List<Usage> findUsageByAssetUUIDAndMaxPrimaryUsageValue(@Param("assetUUID") String assetUUID);
    //
    List <Usage> findUsageByAssetUUID(String assetUUID);
//    @Query(value = "SELECT u from t_usages u where u.primaryUsageValue = (" +
//            " SELECT MAX (uu.primaryUsageValue) from t_usages uu where u.assetUUID=:assetUUID) ")
//    List<Usage> findUsageByAssetUUIDAndMaxPrimaryUsageValue(@Param("assetUUID") String assetUUID);
//    @Query(value = "SELECT u from t_usages u where u.secondaryUsageValue = (" +
//            " SELECT MAX (uu.secondaryUsageValue) from t_usages uu where u.assetUUID=:assetUUID) ")
//    List<Usage> findUsageByAssetUUIDAndMaxSecondaryUsageValue(@Param("assetUUID") String assetUUID);
//

//    @Query(value =  " SELECT MAX (uu.primaryUsageValue) from t_usages uu where uu.assetUUID=:assetUUID ")
//    List<Usage> findUsageByAssetUUIDAndPrimaryUsageValue(@Param("assetUUID") String assetUUID);
//    @Query(value =
//            " SELECT MAX (uu.secondaryUsageValue) from t_usages uu where uu.assetUUID=:assetUUID ")
//    List<Usage> findUsageByAssetUUIDAndSecondaryUsageValue(@Param("assetUUID") String assetUUID);

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
    @Query(value = "SELECT * FROM t_usages  u WHERE u.assetuuid IN :assetUUIDS AND u.category=:category Order BY u.created_at desc \n#pageable\n",
            countQuery = "SELECT count(*) FROM t_usages u WHERE u.assetuuid in :assetUUIDS AND u.category=:category Order BY u.created_at desc \n#pageable\n",nativeQuery = true)
    Page<Usage> findByAssetUUIDInAndCategory(@Param("assetUUIDS") List<String> assetUUIDS,@Param("category") String category, Pageable pageable);
}
