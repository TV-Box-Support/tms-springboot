package com.vnptt.tms.service;

import com.vnptt.tms.dto.HistoryPerformanceDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHistoryPerformanceService {
    HistoryPerformanceDTO save(HistoryPerformanceDTO historyPerformanceDTO);

    HistoryPerformanceDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<HistoryPerformanceDTO> findAll(Pageable pageable);

    List<HistoryPerformanceDTO> findAll();

    List<HistoryPerformanceDTO> findHistoryLater(Long deviceId, int day, long hour, int minutes);
}
