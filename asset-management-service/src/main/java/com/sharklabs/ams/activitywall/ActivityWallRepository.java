package com.sharklabs.ams.activitywall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface ActivityWallRepository extends JpaRepository<ActivityWall,Long> {

    ActivityWall findActivityWallByAssetUuid(String uuid);

    ActivityWall findActivityWallByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);

}
