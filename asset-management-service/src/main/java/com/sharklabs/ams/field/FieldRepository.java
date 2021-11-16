package com.sharklabs.ams.field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.List;

public interface FieldRepository extends JpaRepository<Field,Long> {
    Field findByUuid(String uuid);

    boolean existsByUuidAndLabelContainingIgnoreCase(String uuid,String label);

    @Query("SELECT new com.sharklabs.ams.field.FieldDetailDTO(f.uuid,f.label,f.fieldMetadata,f.type,f.options,f.iconUrl,f.fieldPosition,f.isMandatory,f.fieldTemplateUUID) FROM t_field f WHERE f.id in (ids)")
    List<FieldDetailDTO> findFieldsByCategoriesUUID(@Param("ids") List<Long> ids);

    @Transactional
    Long deleteByUuid(String uuid);
}
