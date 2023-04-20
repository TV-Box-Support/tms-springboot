package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApkRepository extends JpaRepository<ApkEntity, Long>{

    ApkEntity findOneById(Long id);

    List<ApkEntity> findApkEntitiesByPolicyEntitiesId(Long policyId);
}
