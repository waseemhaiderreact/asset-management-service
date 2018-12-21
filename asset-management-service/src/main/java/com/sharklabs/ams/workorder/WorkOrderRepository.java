package com.sharklabs.ams.workorder;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Page<WorkOrder> findByIdNotNull(Pageable page);
}
