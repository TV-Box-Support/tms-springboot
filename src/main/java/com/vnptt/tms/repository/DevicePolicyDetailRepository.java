package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevicePolicyDetailRepository extends JpaRepository<DevicePolicyDetailEntity, Long> {

    DevicePolicyDetailEntity findOneById(Long id);

    DevicePolicyDetailEntity findOneByDeviceEntityDetailIdAndPolicyEntityDetailId(Long deviceId, Long policyId);

    List<DevicePolicyDetailEntity> findAllByDeviceEntityDetailIdOrderByModifiedDateDesc(Long deviceId, Pageable pageable);

    List<DevicePolicyDetailEntity> findAllByPolicyEntityDetailIdOrderByModifiedDateDesc(Long policyId, Pageable pageable);

    List<DevicePolicyDetailEntity> findAllByPolicyEntityDetailIdAndStatusOrderByModifiedDateDesc(Long policyId, Integer status, Pageable pageable);
    List<DevicePolicyDetailEntity> findAllByDeviceEntityDetailIdAndStatusOrderByModifiedDateAsc(Long deviceId, Integer status);
    Long countAllByPolicyEntityDetailId(Long policyId);

    Long countAllByPolicyEntityDetailIdAndStatus(Long policyId, Integer status);

    List<DevicePolicyDetailEntity> findAllByDeviceEntityDetailIdAndStatusOrderByModifiedDateDesc(Long deviceId, Integer status, Pageable pageable);

    Long countAllByDeviceEntityDetailIdAndStatus(Long deviceId, Integer status);

    Long countAllByDeviceEntityDetailId(Long deviceID);

    Long countAllByStatus(int i);
}
