package com.vnptt.tms.repository;

import com.vnptt.tms.entity.CommandEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

    CommandEntity findOneById(Long id);

    CommandEntity findOneByCommand(String command);

    List<CommandEntity> findAllByNameContainingOrderByModifiedDateDesc(String name, Pageable pageable);

    Long countAllByNameContaining(String name);

    List<CommandEntity> findAllByOrderByModifiedDateDesc(Pageable pageable);
}
