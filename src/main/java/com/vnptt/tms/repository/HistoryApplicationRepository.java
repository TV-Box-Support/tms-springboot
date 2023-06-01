package com.vnptt.tms.repository;

import com.vnptt.tms.entity.HistoryApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryApplicationRepository extends JpaRepository<HistoryApplicationEntity, Long> {

    HistoryApplicationEntity findOneById(Long id);

    List<HistoryApplicationEntity> findAllByHistoryDeviceApplicationEntityIdAndCreatedDateBetween(Long deviceApplicationId, LocalDateTime localDateTime, LocalDateTime localDateTimeNow);

    List<HistoryApplicationEntity> findAllByCreatedDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTimeNow);
}
