package com.sharklabs.ams.serviceentry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceEntryRepository extends JpaRepository<ServiceEntry,Long> {
    Page<ServiceEntry> findByIdNotNull(Pageable page);
}
