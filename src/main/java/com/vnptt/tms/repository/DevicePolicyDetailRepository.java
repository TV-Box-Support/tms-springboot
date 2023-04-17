package com.vnptt.tms.repository;

import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import com.vnptt.tms.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevicePolicyDetailRepository extends JpaRepository <DevicePolicyDetailEntity, Long> {

    DevicePolicyDetailEntity findOneById(Long id);

}
