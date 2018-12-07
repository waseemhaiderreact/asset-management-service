package com.sharklabs.ams.servicetask;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTaskRepository extends JpaRepository<ServiceTask,Long> {
    Page<ServiceTask> findByIdNotNull(Pageable page);
}
