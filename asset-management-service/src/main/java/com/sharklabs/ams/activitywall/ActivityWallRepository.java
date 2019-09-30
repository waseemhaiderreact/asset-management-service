package com.sharklabs.ams.activitywall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.ArrayList;

public interface ActivityWallRepository extends JpaRepository<ActivityWall,Long> {

    ActivityWall findActivityWallByAssetUuid(String uuid);

    ActivityWall findActivityWallByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);

    @Transactional
    void deleteAllByAssetUuidIn(ArrayList<String> assetUUIDs);

    @Transactional
    void deleteActivityWallByAssetUuid(String assetUUID);

}
