package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApplicationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findOneById(Long id);

    List<ApplicationEntity> findByPackagenameContainingOrderByCreatedDateDesc(String packagename);

    long countByPackagenameContaining(String packagename);

    ApplicationEntity findOneByPackagenameAndVersion(String packagename, Long version);

    List<ApplicationEntity> findByPackagenameContainingOrderByCreatedDateDesc(String packagename, Pageable pageable);

    List<ApplicationEntity> findByDeviceApplicationEntitiesDeviceAppEntityDetailSnAndDeviceApplicationEntitiesIsalive(String sn, Boolean isalive);

    List<ApplicationEntity> findByDeviceApplicationEntitiesDeviceAppEntityDetailIdAndDeviceApplicationEntitiesIsaliveAndNameContainingAndIssystemOrderByCreatedDateDesc(Long deviceId, Boolean isalive, String name, Boolean isSystem, Pageable pageable);

    List<ApplicationEntity> findTop4ApplicationEntitiesByIssystemOrderByDeviceApplicationEntitiesDesc(boolean isSystem);
}
