package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    DeviceEntity findOneById(Long id);

    DeviceEntity findOneBySn(String sn);

    List<DeviceEntity> findAllByModelContainingAndFirmwareVerContainingOrderByModifiedDateDesc(String model, String firmwareVer);

    List<DeviceEntity> findAllByModelContainingAndFirmwareVerContainingOrderByModifiedDateDesc(String model, String firmwareVer, Pageable pageable);

    List<DeviceEntity> findAllByLocationContainingOrderByModifiedDateDesc(String location);

    List<DeviceEntity> findAllByDateOrderByModifiedDateDesc(Date date);

}
