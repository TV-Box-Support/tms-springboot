package com.vnptt.tms.repository;

import com.vnptt.tms.entity.HistoryPerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryPerformanceRepository extends JpaRepository<HistoryPerformanceEntity, Long> {

    HistoryPerformanceEntity findOneById(Long id);

    List<HistoryPerformanceEntity> findAllByCreatedDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTimeNow);

    List<HistoryPerformanceEntity> findAllByDeviceEntityHistoryIdAndAndCreatedDateBetween(Long deviceId, LocalDateTime localDateTime, LocalDateTime localDateTimeNow);

}
