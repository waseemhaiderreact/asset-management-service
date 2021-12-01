package com.sharklabs.ams.field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.List;

public interface FieldRepository extends JpaRepository<Field,Long> {
    Field findByUuid(String uuid);

    Field findFieldByLabelAndFie(String label);

    boolean existsByUuidAndLabelContainingIgnoreCase(String uuid,String label);

    @Transactional
    Long deleteByUuid(String uuid);
}
