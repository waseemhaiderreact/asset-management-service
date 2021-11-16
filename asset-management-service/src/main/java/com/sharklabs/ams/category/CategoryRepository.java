package com.sharklabs.ams.category;

import com.sharklabs.ams.field.FieldDetailDTO;
import com.sharklabs.ams.fieldtemplate.FieldTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findCategoryByUuid(String uuid);

    Category findByAssetsUuid(String uuid);

    @Query("SELECT c.name FROM t_category c WHERE c.uuid = ?1")
    String getCategoryNameByUUID(@Param("uuid") String uuid);

    List<Category> findByTenantUUID(String tenantUUID);

    @Query("SELECT new com.sharklabs.ams.category.CategoryDTO(c.uuid,c.name) FROM t_category c WHERE c.tenantUUID=?1")
    List<CategoryDTO> findCategoriesListByTenantUUID(@Param("uuid") String uuid);

    @Query("SELECT new com.sharklabs.ams.category.CategoryAndFieldDTO(c.uuid,c.name) FROM t_category c WHERE c.tenantUUID=?1")
    List<CategoryAndFieldDTO> findCategoriesAndFieldsByTenantUUID(@Param("uuid") String tenantUUID);

    @Query("SELECT c.fieldTemplate.id from t_category c WHERE c.tenantUUID=?1")
    List<Long> findCategoryUUIDByTenantUUID(@Param("uuid") String tenantUUID);



    Category findByTenantUUIDAndName(String tenantUUID, String name);

    @Modifying
    @Transactional
//    @Query(value = "delete from t_category where uuid=?1",nativeQuery = true)
    Integer deleteById(Long id);
}
