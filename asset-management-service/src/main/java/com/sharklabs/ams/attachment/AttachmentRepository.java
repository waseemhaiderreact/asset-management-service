package com.sharklabs.ams.attachment;

import com.sharklabs.ams.AssetImage.AssetImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    Set<Attachment> findAllByAssetUUID(String uuid);

    Attachment findById(Long id);

}
