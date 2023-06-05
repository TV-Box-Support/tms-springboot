package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ListDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ListDeviceRepository extends JpaRepository<ListDeviceEntity, Long> {

    ListDeviceEntity findOneById(Long id);

    ListDeviceEntity findOneByName(String all);

    List<ListDeviceEntity> findAllByUserEntitiesListDeviceId(Long roleManagementId);

}
