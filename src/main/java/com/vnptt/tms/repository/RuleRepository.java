package com.vnptt.tms.repository;

import com.vnptt.tms.entity.RuleEntity;
import com.vnptt.tms.config.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<RuleEntity, Long> {

    RuleEntity findOneByName(String name);

    RuleEntity findOneById(Long id);

    RuleEntity findByName(ERole eRole);
}
