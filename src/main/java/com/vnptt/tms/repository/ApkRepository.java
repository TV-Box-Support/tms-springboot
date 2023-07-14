package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApkEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApkRepository extends JpaRepository<ApkEntity, Long> {

    ApkEntity findOneById(Long id);

    List<ApkEntity> findApkEntitiesByPolicyEntitiesIdOrderByModifiedDateDesc(Long policyId);

    List<ApkEntity> findAllByPackagenameContainingAndVersionContainingOrderByModifiedDateDesc(String packagename, String version, Pageable pageable);

    Long countByPackagenameContainingAndVersionContainingOrderByModifiedDateDesc(String packagename, String version);

}
