package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeviceApplicationRepository extends JpaRepository<DeviceApplicationEntity, Long> {

    DeviceApplicationEntity findOneById(Long id);

    DeviceApplicationEntity findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(Long deviceId, Long applicationId);

    List<DeviceApplicationEntity> findByDeviceAppEntityDetailId(Long deviceId);

    List<DeviceApplicationEntity> findByDeviceAppEntityDetailIdAndIsalive(Long deviceId, boolean isAlive);

    List<DeviceApplicationEntity> findByApplicationEntityDetailId(Long applicationId);

}
