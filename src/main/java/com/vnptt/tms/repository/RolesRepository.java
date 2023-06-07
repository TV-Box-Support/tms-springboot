package com.vnptt.tms.repository;

import com.vnptt.tms.config.ERole;
import com.vnptt.tms.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    RolesEntity findOneByName(ERole name);

    RolesEntity findOneById(Long id);

    RolesEntity findByName(ERole eRole);
}
