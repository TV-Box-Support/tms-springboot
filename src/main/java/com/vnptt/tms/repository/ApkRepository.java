package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ApkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApkRepository extends JpaRepository<ApkEntity, Long>{

    ApkEntity findOneById(Long id);
}
