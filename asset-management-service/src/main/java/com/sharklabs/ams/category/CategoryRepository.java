package com.sharklabs.ams.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findCategoryByUuid(String uuid);

    List<Category> findByTenantUUID(String tenantUUID);

    @Modifying
    @Transactional
//    @Query(value = "delete from t_category where uuid=?1",nativeQuery = true)
    Integer deleteById(Long id);
}
