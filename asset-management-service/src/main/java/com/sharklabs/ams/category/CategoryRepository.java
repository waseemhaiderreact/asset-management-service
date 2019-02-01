package com.sharklabs.ams.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findCategoryByUuid(String uuid);

    @Modifying
    @Transactional
//    @Query(value = "delete from t_category where uuid=?1",nativeQuery = true)
    Integer deleteById(Long id);
}
