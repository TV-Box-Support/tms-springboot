package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import com.vnptt.tms.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevicePolicyDetailRepository extends JpaRepository <DevicePolicyDetailEntity, Long> {

    DevicePolicyDetailEntity findOneById(Long id);

    List<DevicePolicyDetailEntity> findAllByDeviceEntityDetailId(Long deviceId);

    List<DevicePolicyDetailEntity> findAllByPolicyEntityDetailId(Long policyId);

}
