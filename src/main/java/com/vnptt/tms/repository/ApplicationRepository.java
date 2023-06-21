package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApplicationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findOneById(Long id);

    List<ApplicationEntity> findByPackagenameContainingOrderByModifiedDateDesc(String packagename);

    long countByPackagenameContaining(String packagename);

    ApplicationEntity findOneByPackagenameAndVersion(String packagename, String version);

    List<ApplicationEntity> findByPackagenameContainingOrderByModifiedDateDesc(String packagename, Pageable pageable);

    List<ApplicationEntity> findByDeviceApplicationEntitiesDeviceAppEntityDetailSnAndDeviceApplicationEntitiesIsalive(String sn, Boolean isalive);

    List<ApplicationEntity> findByDeviceApplicationEntitiesDeviceAppEntityDetailIdAndDeviceApplicationEntitiesIsaliveAndNameContainingAndIssystemOrderByModifiedDateDesc(Long deviceId, Boolean isalive, String name, Boolean isSystem, Pageable pageable);
}
