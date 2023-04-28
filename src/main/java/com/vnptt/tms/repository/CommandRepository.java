package com.vnptt.tms.repository;

import com.vnptt.tms.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

    CommandEntity findOneById(Long id);

    CommandEntity findOneByCommand(String command);

}
