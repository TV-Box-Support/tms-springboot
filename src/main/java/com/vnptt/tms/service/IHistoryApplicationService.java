package com.vnptt.tms.service;

import com.vnptt.tms.dto.HistoryApplicationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHistoryApplicationService {
    HistoryApplicationDTO save(HistoryApplicationDTO historyApplicationDTO, String sn, String packagename);
    HistoryApplicationDTO findOne(Long id);
    int totalItem();
    void delete(Long[] ids);
    List<HistoryApplicationDTO> findAll(Pageable pageable);
    List<HistoryApplicationDTO> findAll();

    List<HistoryApplicationDTO> findHistoryAppDeviceLater(Long deviceApplicationId, int day, long hour, int minutes);

}
