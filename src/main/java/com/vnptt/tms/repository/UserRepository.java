package com.vnptt.tms.repository;

import com.vnptt.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

    UserEntity findOneById(Long id);

    List<UserEntity> findUserEntitiesByRuleEntityId(Long ruleId);
}
