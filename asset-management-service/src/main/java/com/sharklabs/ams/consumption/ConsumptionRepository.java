package com.sharklabs.ams.consumption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {
    Consumption findConsumptionByUuid(String uuid);


    @Modifying
    @Transactional
    Integer deleteById(Long id);
}
