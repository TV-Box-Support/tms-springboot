package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevicePolicyDetailRepository extends JpaRepository<DevicePolicyDetailEntity, Long> {

    DevicePolicyDetailEntity findOneById(Long id);

    List<DevicePolicyDetailEntity> findAllByDeviceEntityDetailIdOrderByModifiedDateDesc(Long deviceId);

    List<DevicePolicyDetailEntity> findAllByPolicyEntityDetailIdOrderByModifiedDateDesc(Long policyId);

}
