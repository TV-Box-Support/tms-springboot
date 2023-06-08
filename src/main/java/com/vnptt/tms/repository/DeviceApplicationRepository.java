package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceApplicationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeviceApplicationRepository extends JpaRepository<DeviceApplicationEntity, Long> {

    DeviceApplicationEntity findOneById(Long id);

    DeviceApplicationEntity findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(Long deviceId, Long applicationId);

    DeviceApplicationEntity findDeviceApplicationEntityByDeviceAppEntityDetailSnAndApplicationEntityDetailId(String sn, Long applicationId);

    List<DeviceApplicationEntity> findByDeviceAppEntityDetailIdOrderByModifiedDateDesc(Long deviceId, Pageable pageable);

    List<DeviceApplicationEntity> findByDeviceAppEntityDetailIdAndIsaliveAndApplicationEntityDetailNameContainingOrderByModifiedDateDesc(Long deviceId, boolean isAlive, String name, Pageable pageable);

    Long countByDeviceAppEntityDetailId(Long deviceId);

    Long countByDeviceAppEntityDetailIdAndIsaliveAndApplicationEntityDetailNameContaining(Long deviceId, boolean isAlive, String name);

    List<DeviceApplicationEntity> findAllByApplicationEntityDetailIdOrderByModifiedDateDesc(Long applicationId);

    List<DeviceApplicationEntity> findAllByApplicationEntityDetailNameOrderByModifiedDateDesc(String name, Pageable pageable);

    Long countByApplicationEntityDetailNameContaining(String name);

}
