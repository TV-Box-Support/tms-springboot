package com.vnptt.tms.repository;

import com.vnptt.tms.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findOneById(Long id);

    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    List<UserEntity> findAllByActiveOrderByModifiedDateDesc(Pageable pageable, boolean active);

    List<UserEntity> findAllByActiveOrderByModifiedDateDesc(boolean active);
    //findAllWithNameOrEmailOrUsername
    List<UserEntity> findAllByActiveOrNameContainingOrEmailContainingOrUsernameContainingOrCompanyContainingOrderByModifiedDateDesc(Pageable pageable, boolean active, String name, String email, String username, String company);
}
