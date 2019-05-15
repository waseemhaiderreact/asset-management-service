package com.sharklabs.ams.imagevoice;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ImageVoiceRepository extends JpaRepository<ImageVoice,Long>{

}
