package com.vnptt.tms.repository;

import com.vnptt.tms.config.ERoleFunction;
import com.vnptt.tms.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    RolesEntity findOneByName(String name);

    RolesEntity findOneById(Long id);

    RolesEntity findByName(ERoleFunction eRoleFunction);
}
