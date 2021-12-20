package com.sharklabs.ams.category;

import com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModel;
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

    @Query("SELECT new com.sharklabs.ams.category.CategoryFieldDTO(c.name,c.uuid,f) FROM t_category c, t_field_template f WHERE c.tenantUUID=?1 AND c.id = f.category.id")
    List<CategoryFieldDTO> findCategoriesFieldsByTenantUUID(@Param("uuid") String uuid);

    Category findByTenantUUIDAndName(String tenantUUID, String name);

    @Query("SELECT new com.sharklabs.ams.events.assetBasicDetail.AssetBasicDetailModel(c.name,c.uuid) FROM t_category c WHERE c.tenantUUID=?1")
    List<AssetBasicDetailModel> findCategoriesByTenantUUID(@Param("uuid") String uuid);

    @Modifying
    @Transactional
//    @Query(value = "delete from t_category where uuid=?1",nativeQuery = true)
    Integer deleteById(Long id);
}
