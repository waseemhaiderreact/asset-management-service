package com.sharklabs.ams.AssetImage;

import com.sharklabs.ams.assetfield.AssetField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AssetImageRepository extends JpaRepository<AssetImage,Long> {
    Set<AssetImage> findAllByAssetUuid(String uuid);
}
