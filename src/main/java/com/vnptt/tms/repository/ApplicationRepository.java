package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApplicationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findOneById(Long id);

    List<ApplicationEntity> findByPackagenameContaining(String packagename);

    ApplicationEntity findOneByPackagenameAndVersion(String packagename, String version);

    List<ApplicationEntity> findByPackagenameContaining(String packagename, Pageable pageable);

}
