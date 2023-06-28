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

    Long countDistinctByHistoryPerformanceEntitiesIsNull();

    Long countByHdmiBetween(Integer lower, Integer upper);

    Long countByNetworkContaining(String network);

    List<DeviceEntity> findAllByLocationContainingOrDescriptionContainingOrderByModifiedDateDesc(String location, String description, Pageable pageable);

    Long countByLocationContainingOrDescriptionContaining(String location, String description);

    Long countByLocationContaining(String location);
}
