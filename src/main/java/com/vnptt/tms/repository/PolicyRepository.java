package com.vnptt.tms.repository;

import com.vnptt.tms.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    PolicyEntity findOneById(Long id);

    PolicyEntity findOneByPolicyname(String policyname);

    List<PolicyEntity> findAllByCommandEntityIdOrderByModifiedDateDesc(Long commandId);

    List<PolicyEntity> findPolicyEntitiesByApkEntitiesPolicyIdOrderByModifiedDateDesc(Long apkId);

    List<PolicyEntity> findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailIdOrderByModifiedDateDesc(Long deviceId);

}
