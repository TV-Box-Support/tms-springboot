package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeviceApplicationRepository extends JpaRepository<DeviceApplicationEntity, Long> {

    DeviceApplicationEntity findOneById(Long id);

    DeviceApplicationEntity findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(Long deviceId, Long applicationId);

    List<DeviceApplicationEntity> findByDeviceAppEntityDetailIdOrderByModifiedDateDesc(Long deviceId);

    List<DeviceApplicationEntity> findByDeviceAppEntityDetailIdAndIsaliveOrderByModifiedDateDesc(Long deviceId, boolean isAlive);

    List<DeviceApplicationEntity> findAllByApplicationEntityDetailIdOrderByModifiedDateDesc (Long applicationId);

}
