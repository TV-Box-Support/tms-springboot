package com.vnptt.tms.repository;

import com.vnptt.tms.entity.HistoryPerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryPerformanceRepository extends JpaRepository <HistoryPerformanceEntity, Long> {

    HistoryPerformanceEntity findOneById(Long id);

}
