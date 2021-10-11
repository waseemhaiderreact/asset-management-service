package com.sharklabs.ams.field;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import javax.xml.transform.sax.SAXTransformerFactory;

public interface FieldRepository extends JpaRepository<Field,Long> {
    Field findByUuid(String uuid);

    boolean existsByUuidAndLabelContainingIgnoreCase(String uuid,String label);

    @Transactional
    Long deleteByUuid(String uuid);
}
