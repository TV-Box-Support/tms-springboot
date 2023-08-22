package com.vnptt.tms.repository;

import com.vnptt.tms.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository là một interface trong Spring Data JPA cung cấp các phương thức tiêu chuẩn để thực hiện các thao tác
// CRUD (Create, Read, Update, Delete) trên đối tượng ánh xạ. Nó cung cấp các phương thức như save, findById, findAll,
// delete, v.v.
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findOneById(Long id);

    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    List<UserEntity> findAllByActiveOrderByModifiedDateDesc(Pageable pageable, boolean active);

    List<UserEntity> findAllByActiveOrderByModifiedDateDesc(boolean active);

    Long countAllByActiveAndNameContainingOrEmailContainingOrUsernameContainingOrCompanyContaining(boolean active, String name, String email, String username, String company);

    Long countAllByActive(boolean active);

    List<UserEntity> findAllByActiveAndNameContainingOrEmailContainingOrUsernameContainingOrCompanyContainingOrderByModifiedDateDesc(Pageable pageable, boolean active, String name, String email, String username, String company);

    List<UserEntity> findAllByDeviceEntitiesIdOrderByModifiedDateDesc(Long listDeviceId, Pageable pageable);

    List<UserEntity> findAllByDeviceEntitiesIdAndNameContainingOrderByModifiedDateDesc(Long listDeviceId, String name, Pageable pageable);

    Long countByDeviceEntitiesId(Long listDeviceId);

    Long countByDeviceEntitiesIdAndNameContaining(Long listDeviceId, String name);
}
