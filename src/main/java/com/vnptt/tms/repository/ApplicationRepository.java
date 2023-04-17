package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findOneById(Long id);
}
