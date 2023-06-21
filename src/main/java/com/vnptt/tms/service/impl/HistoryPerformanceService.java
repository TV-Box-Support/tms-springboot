package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.HistoryPerformanceConverter;
import com.vnptt.tms.converter.PolicyConverter;
import com.vnptt.tms.dto.HistoryPerformanceDTO;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.HistoryPerformanceEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.HistoryPerformanceRepository;
import com.vnptt.tms.repository.PolicyRepository;
import com.vnptt.tms.service.IHistoryPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class HistoryPerformanceService implements IHistoryPerformanceService {

    @Autowired
    private HistoryPerformanceRepository historyPerformanceRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private PolicyConverter policyConverter;

    @Autowired
    private HistoryPerformanceConverter historyPerformanceConverter;

    @Override
    public List<PolicyDTO> save(HistoryPerformanceDTO historyPerformanceDTO) {
        HistoryPerformanceEntity historyPerformanceEntity = historyPerformanceConverter.toEntity(historyPerformanceDTO);
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(historyPerformanceDTO.getDevicesn());
        if (deviceEntity == null){
            throw new ResourceNotFoundException("Can not find device with sn " + historyPerformanceDTO.getDevicesn());
        }
        try {
            historyPerformanceEntity.setDeviceEntityHistory(deviceEntity);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.toString());
        }
        historyPerformanceRepository.save(historyPerformanceEntity);

        // return policy for box
        List<PolicyDTO> result = new ArrayList<>();
        List<PolicyEntity> policyEntities = policyRepository.findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailIdOrderByModifiedDateDesc(deviceEntity.getId());
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    @Override
    public HistoryPerformanceDTO findOne(Long id) {
        HistoryPerformanceEntity entity = historyPerformanceRepository.findOneById(id);
        return historyPerformanceConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<HistoryPerformanceDTO> findAll(Pageable pageable) {
        List<HistoryPerformanceEntity> entities = historyPerformanceRepository.findAll(pageable).getContent();
        List<HistoryPerformanceDTO> result = new ArrayList<>();
        for (HistoryPerformanceEntity item : entities) {
            HistoryPerformanceDTO historyPerformanceDTO = historyPerformanceConverter.toDTO(item);
            result.add(historyPerformanceDTO);
        }
        return result;
    }

    @Override
    public List<HistoryPerformanceDTO> findAll() {
        List<HistoryPerformanceEntity> entities = historyPerformanceRepository.findAll();
        List<HistoryPerformanceDTO> result = new ArrayList<>();
        for (HistoryPerformanceEntity item : entities) {
            HistoryPerformanceDTO historyPerformanceDTO = historyPerformanceConverter.toDTO(item);
            result.add(historyPerformanceDTO);
        }
        return result;
    }

    /**
     * find all history performan of device
     *
     * @param deviceId
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @Override
    public List<HistoryPerformanceDTO> findHistoryLater(Long deviceId, int day, long hour, int minutes) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<HistoryPerformanceDTO> result = new ArrayList<>();
        List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-minutes).plusDays(-day).plusHours(-hour);
        historyPerformanceEntities = historyPerformanceRepository.findAllByDeviceEntityHistoryIdAndAndCreatedDateBetweenOrderByModifiedDateDesc(deviceId, time, LocalDateTime.now());
        for (HistoryPerformanceEntity iteam : historyPerformanceEntities) {
            HistoryPerformanceDTO historyPerformanceDTO = historyPerformanceConverter.toDTO(iteam);
            result.add(historyPerformanceDTO);
        }
        return result;
    }


    @Override
    public int totalItem() {
        return (int) historyPerformanceRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            historyPerformanceRepository.deleteById(item);
        }
    }
}
