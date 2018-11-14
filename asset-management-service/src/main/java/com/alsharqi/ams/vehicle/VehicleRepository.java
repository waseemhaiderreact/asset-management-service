package com.alsharqi.ams.vehicle;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByAssetNumber(String assetNumber);
    @Modifying
    @Transactional
    @Query(value = "delete from t_vehicle where asset_number=?1",nativeQuery = true)
    Integer deleteByAssetNumber(String assetNumber);
    Page<Vehicle> findByIdNotNull(Pageable page);
}
