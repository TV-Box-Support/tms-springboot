package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    DeviceEntity findOneById(Long id);

    DeviceEntity findOneBySn(String sn);

    List<DeviceEntity> findAllByModelContainingAndFirmwareVerContainingOrderByModifiedDateDesc(String model, String firmwareVer);

    List<DeviceEntity> findAllByModelContainingAndFirmwareVerContainingOrderByModifiedDateDesc(String model, String firmwareVer, Pageable pageable);

    List<DeviceEntity> findAllByLocationContainingOrderByModifiedDateDesc(String location);

    List<DeviceEntity> findAllByDateOrderByModifiedDateDesc(Date date);

    Long countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(@Param("localDateTime") LocalDateTime localDateTime, @Param("localDateTimeNow") LocalDateTime localDateTimeNow );

}
