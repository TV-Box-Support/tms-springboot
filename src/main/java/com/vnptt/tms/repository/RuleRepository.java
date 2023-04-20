package com.vnptt.tms.repository;

import com.vnptt.tms.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuleRepository extends JpaRepository<RuleEntity, Long> {

    RuleEntity findOneByName(String name);

    RuleEntity findOneById(Long id);

}
