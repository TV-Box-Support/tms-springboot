package com.vnptt.tms.repository;

import com.vnptt.tms.entity.PolicyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    PolicyEntity findOneById(Long id);

    List<PolicyEntity> findAllByPolicynameContaining(String policyname);

    Long countAllByPolicynameContaining(String policyname);

    List<PolicyEntity> findAllByCommandEntityIdOrderByModifiedDateDesc(Long commandId);

    List<PolicyEntity> findPolicyEntitiesByApkEntitiesPolicyIdOrderByModifiedDateDesc(Long apkId);

    List<PolicyEntity> findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailIdOrderByModifiedDateDesc(Long deviceId);

    List<PolicyEntity> findAllByPolicynameContaining(String policyname, Pageable pageable);

    PolicyEntity findOneByDevicePolicyDetailEntitiesId(Long policyDetailId);
}
