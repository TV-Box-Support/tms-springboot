package com.vnptt.tms.repository;

import com.vnptt.tms.entity.HistoryApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryApplicationRepository extends JpaRepository <HistoryApplicationEntity, Long> {

    HistoryApplicationEntity findOneById(Long id);

}
