package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    DeviceEntity findOneById(Long id);

    DeviceEntity findOneBySn(String sn);

    List<DeviceEntity> findAllByDescriptionContainingOrSnContainingOrderByModifiedDateDesc(String description, String sn, Pageable pageable);

    Long countByDescriptionContainingOrSnContaining(String description, String sn);

    List<DeviceEntity> findAllByLocationContainingOrderByModifiedDateDesc(String location, Pageable pageable);

    Long countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(@Param("localDateTime") LocalDateTime localDateTime, @Param("localDateTimeNow") LocalDateTime localDateTimeNow);

    Long countDistinctBySnContainingAndHistoryPerformanceEntitiesCreatedDateBetween(String sn, @Param("localDateTime") LocalDateTime localDateTime, @Param("localDateTimeNow") LocalDateTime localDateTimeNow);

    Long countDistinctByHistoryPerformanceEntitiesIsNull();

    Long countByHdmiBetween(Integer lower, Integer upper);

    Long countByNetworkContaining(String network);

    List<DeviceEntity> findAllByLocationContainingOrDescriptionContainingOrderByModifiedDateDesc(String location, String description, Pageable pageable);

    Long countByLocationContainingOrDescriptionContaining(String location, String description);

    Long countByLocationContaining(String location);

    List<DeviceEntity> findAllByDevicePolicyDetailEntitiesPolicyEntityDetailIdOrderByModifiedDateDesc(Long policyId, Pageable pageable);

    List<DeviceEntity> findAllByDevicePolicyDetailEntitiesPolicyEntityDetailIdAndSnContainingOrderByModifiedDateDesc(Long policyId, String sn, Pageable pageable);

    Long countByDevicePolicyDetailEntitiesPolicyEntityDetailId(Long policyId);
    Long countByDevicePolicyDetailEntitiesPolicyEntityDetailIdAndSnContaining(Long policyId, String sn);

    List<DeviceEntity> findTop5BySnContainingOrderByModifiedDateDesc(String search);

    List<DeviceEntity> findAllByListDeviceDetailIdOrderByModifiedDateDesc(Long listDeviceId, Pageable pageable);

    List<DeviceEntity> findAllByListDeviceDetailIdAndSnContainingOrderByModifiedDateDesc(Long listDeviceId, String sn, Pageable pageable);

    Long countAllByListDeviceDetailId(Long listDeviceId);

    Long countByListDeviceDetailIdAndSnContaining(Long listDeviceId, String sn);

    List<DeviceEntity> findAllByOrderByModifiedDateDesc(Pageable pageable);
}
