package com.vnptt.tms.repository;

import com.vnptt.tms.config.ERoleFunction;
import com.vnptt.tms.entity.RoleManagementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleManagementRepository extends JpaRepository<RoleManagementEntity, Long> {

    RoleManagementEntity findOneByName(String name);

    RoleManagementEntity findOneById(Long id);

    RoleManagementEntity findByName(ERoleFunction eRoleFunction);
}
