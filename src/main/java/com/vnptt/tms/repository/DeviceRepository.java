package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    DeviceEntity findOneById(Long id);
    DeviceEntity findOneBySn(String sn);

}
