package com.vnptt.tms.repository;

import com.vnptt.tms.config.ERoleFunction;
import com.vnptt.tms.entity.RoleFunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleFunctionRepository extends JpaRepository<RoleFunctionEntity, Long> {

    RoleFunctionEntity findOneByName(String name);

    RoleFunctionEntity findOneById(Long id);

    RoleFunctionEntity findByName(ERoleFunction eRoleFunction);
}
