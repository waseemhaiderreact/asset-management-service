package com.sharklabs.ams.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Message findMessageByUuid(String uuid);

    @Modifying
    @Transactional
    Integer deleteById(Long id);
}
