package com.vnptt.tms.repository;

import com.vnptt.tms.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository <PolicyEntity, Long> {

    PolicyEntity findOneById(Long id);
    PolicyEntity findOneByPolicyname(String policyname);
    List<PolicyEntity> findAllByCommandEntityId(Long commandId);

    List<PolicyEntity> findPolicyEntitiesByApkEntitiesPolicyId(Long apkId);

    List<PolicyEntity> findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailId(Long deviceId);

}
