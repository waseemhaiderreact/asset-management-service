package com.sharklabs.ams.fieldtemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface FieldTemplateRepository extends CrudRepository<FieldTemplate,Long> {

//    @Query("SELECT fieldTemplate FROM FieldTemplate fieldTemplate WHERE fieldTemplate.uuid=:value")
    FieldTemplate findByUuid(@Param("value")String value);

    @Modifying
    @Transactional
//    @Query(value = "delete from t_field_template where uuid=?1",nativeQuery = true)
    Integer deleteByUuid(String uuid);
}
