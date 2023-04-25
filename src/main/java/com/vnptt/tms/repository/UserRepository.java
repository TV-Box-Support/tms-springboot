package com.vnptt.tms.repository;

import com.vnptt.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findOneById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String mail);

    UserEntity findByUsername(String username);
}
