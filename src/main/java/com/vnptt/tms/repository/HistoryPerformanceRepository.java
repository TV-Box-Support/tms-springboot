package com.vnptt.tms.repository;

import com.vnptt.tms.entity.HistoryPerformanceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryPerformanceRepository extends JpaRepository<HistoryPerformanceEntity, Long> {


    HistoryPerformanceEntity findOneById(Long id);

    List<HistoryPerformanceEntity> findAllByCreatedDateBetweenOrderByCreatedDateDesc(LocalDateTime localDateTime, LocalDateTime localDateTimeNow, Pageable pageable);

    List<HistoryPerformanceEntity> findAllByDeviceEntityHistorySnContainingAndCreatedDateBetweenOrderByCreatedDateDesc(String sn, LocalDateTime localDateTime, LocalDateTime localDateTimeNow, Pageable pageable);


    List<HistoryPerformanceEntity> findAllByDeviceEntityHistoryIdAndAndCreatedDateBetweenOrderByCreatedDateDesc(Long deviceId, LocalDateTime localDateTime, LocalDateTime localDateTimeNow);

    List<HistoryPerformanceEntity> findAllByDeviceEntityHistoryIdAndAndCreatedDateBetweenOrderByCreatedDateAsc(Long deviceId, LocalDateTime localDateTime, LocalDateTime localDateTimeNow);

    Long countByDeviceEntityHistoryIdAndAndCreatedDateBetween(Long deviceId, LocalDateTime start, LocalDateTime End);

}
