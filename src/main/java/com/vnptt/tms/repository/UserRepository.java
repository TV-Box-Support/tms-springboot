package com.vnptt.tms.repository;

import com.vnptt.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

    @Override
    Optional<UserEntity> findById(Long id);

    UserEntity findOneById(Long id);
}
