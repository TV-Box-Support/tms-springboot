package com.vnptt.tms.repository;

import com.vnptt.tms.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository <PolicyEntity, Long> {

    PolicyEntity findOneById(Long id);

    PolicyEntity findOneByPolicyname(String policyname);

}
