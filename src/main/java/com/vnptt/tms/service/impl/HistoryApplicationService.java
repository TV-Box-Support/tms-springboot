package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.HistoryApplicationConverter;
import com.vnptt.tms.dto.HistoryApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.HistoryApplicationEntity;
import com.vnptt.tms.entity.HistoryPerformanceEntity;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.HistoryApplicationRepository;
import com.vnptt.tms.repository.HistoryPerformanceRepository;
import com.vnptt.tms.service.IHistoryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class HistoryApplicationService implements IHistoryApplicationService {

    @Autowired
    private HistoryApplicationRepository historyApplicationRepository;

    @Autowired
    private HistoryPerformanceRepository historyPerformanceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private HistoryApplicationConverter historyApplicationConverter;

    @Override
    public HistoryApplicationDTO save(HistoryApplicationDTO historyApplicationDTO) {
        HistoryApplicationEntity historyApplicationEntity = new HistoryApplicationEntity();
        if (historyApplicationDTO.getId() != null){
            Optional <HistoryApplicationEntity> oldHistoryApplicationEntity = historyApplicationRepository.findById(historyApplicationDTO.getId());
            historyApplicationEntity = historyApplicationConverter.toEntity(historyApplicationDTO, oldHistoryApplicationEntity.get());
        } else {
            historyApplicationEntity = historyApplicationConverter.toEntity(historyApplicationDTO);
        }
        HistoryPerformanceEntity historyPerformanceEntity = historyPerformanceRepository.findOneById(historyApplicationDTO.getHistoryPerId());
        historyApplicationEntity.setHistoryPerformanceEntityHistory(historyPerformanceEntity);
        ApplicationEntity applicationEntity = applicationRepository.findOneById(historyApplicationDTO.getApplicationId());
        historyApplicationEntity.setApplicationEntityHistory(applicationEntity);
        historyApplicationEntity = historyApplicationRepository.save(historyApplicationEntity);
        return historyApplicationConverter.toDTO(historyApplicationEntity);
    }


    @Override
    public HistoryApplicationDTO findOne(Long id) {
        HistoryApplicationEntity entity = historyApplicationRepository.findOneById(id);
        return historyApplicationConverter.toDTO(entity);
    }

    /**
     *  find item with page number and totalPage number
     * @param pageable
     * @return
     */
    @Override
    public List<HistoryApplicationDTO> findAll(Pageable pageable) {
        List<HistoryApplicationEntity> entities = historyApplicationRepository.findAll(pageable).getContent();
        List<HistoryApplicationDTO> result = new ArrayList<>();
        for(HistoryApplicationEntity item : entities){
            HistoryApplicationDTO historyApplicationDTO = historyApplicationConverter.toDTO(item);
            result.add(historyApplicationDTO);
        }
        return result;
    }

    @Override
    public List<HistoryApplicationDTO> findAll() {
        List<HistoryApplicationEntity> entities = historyApplicationRepository.findAll();
        List<HistoryApplicationDTO> result = new ArrayList<>();
        for(HistoryApplicationEntity item : entities){
            HistoryApplicationDTO historyApplicationDTO = historyApplicationConverter.toDTO(item);
            result.add(historyApplicationDTO);
        }
        return result;
    }

    @Override
    public int totalItem(){
        return (int) historyApplicationRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            historyApplicationRepository.deleteById(item);
        }
    }
}
