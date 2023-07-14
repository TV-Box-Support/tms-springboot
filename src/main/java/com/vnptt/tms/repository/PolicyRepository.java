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

    List<PolicyEntity> findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailIdOrderByModifiedDateDesc(Long deviceId, Pageable pageable);

    List<PolicyEntity> findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailIdAndPolicynameContainingOrderByModifiedDateDesc(Long deviceId, String policyname, Pageable pageable);

    Long countAllByDevicePolicyDetailEntitiesDeviceEntityDetailId(Long deviceId);

    Long  countAllByDevicePolicyDetailEntitiesDeviceEntityDetailIdAndPolicynameContaining(Long deviceId, String policyname);

    List<PolicyEntity> findAllByPolicynameContaining(String policyname, Pageable pageable);

    PolicyEntity findOneByDevicePolicyDetailEntitiesId(Long policyDetailId);

}
