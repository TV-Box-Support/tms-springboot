package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ListDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ListDeviceRepository extends JpaRepository<ListDeviceEntity, Long> {

    ListDeviceEntity findOneById(Long id);

//    ListDeviceEntity findListDeviceEntityByDeviceEntityListDeviceIdAndUserEntityListDeviceId(Long deviceId, Long userId);
//
//    List<ListDeviceEntity> findByDeviceAppEntityDetailIdOrderByModifiedDateDesc(Long deviceId);
//
//    List<ListDeviceEntity> findByDeviceAppEntityDetailIdAndIsaliveOrderByModifiedDateDesc(Long deviceId, boolean isAlive);
//
//    List<ListDeviceEntity> findAllByApplicationEntityDetailIdOrderByModifiedDateDesc (Long applicationId);

}
