package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.HistoryApplicationConverter;
import com.vnptt.tms.dto.HistoryApplicationDTO;
import com.vnptt.tms.entity.DeviceApplicationEntity;
import com.vnptt.tms.entity.HistoryApplicationEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.DeviceApplicationRepository;
import com.vnptt.tms.repository.HistoryApplicationRepository;
import com.vnptt.tms.service.IHistoryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class HistoryApplicationService implements IHistoryApplicationService {

    @Autowired
    private HistoryApplicationRepository historyApplicationRepository;

    @Autowired
    private DeviceApplicationRepository deviceApplicationRepository;

    @Autowired
    private HistoryApplicationConverter historyApplicationConverter;

    /**
     * save history application, if application is still in device
     *
     * @param historyApplicationDTO
     * @param sn
     * @param packagename
     * @param version
     * @return
     */
    @Override
    public HistoryApplicationDTO save(HistoryApplicationDTO historyApplicationDTO, String sn, String packagename, Long version) {

        HistoryApplicationEntity historyApplicationEntity = historyApplicationConverter.toEntity(historyApplicationDTO);
        DeviceApplicationEntity deviceApplicationEntity = deviceApplicationRepository.findOneByDeviceAppEntityDetailSnAndApplicationEntityDetailPackagenameAndApplicationEntityDetailVersionAndIsalive(sn, packagename, version, true);

        if (deviceApplicationEntity == null) {
            throw new ResourceNotFoundException("not found application info with packagename " + packagename + " in device with serialnumber " + sn);
        }

        historyApplicationEntity.setHistoryDeviceApplicationEntity(deviceApplicationEntity);
        historyApplicationEntity = historyApplicationRepository.save(historyApplicationEntity);
        return historyApplicationConverter.toDTO(historyApplicationEntity);
    }


    @Override
    public HistoryApplicationDTO findOne(Long id) {
        HistoryApplicationEntity entity = historyApplicationRepository.findOneById(id);
        return historyApplicationConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<HistoryApplicationDTO> findAll(Pageable pageable) {
        List<HistoryApplicationEntity> entities = historyApplicationRepository.findAll(pageable).getContent();
        List<HistoryApplicationDTO> result = new ArrayList<>();
        for (HistoryApplicationEntity item : entities) {
            HistoryApplicationDTO historyApplicationDTO = historyApplicationConverter.toDTO(item);
            result.add(historyApplicationDTO);
        }
        return result;
    }

    @Override
    public List<HistoryApplicationDTO> findAll() {
        List<HistoryApplicationEntity> entities = historyApplicationRepository.findAll();
        List<HistoryApplicationDTO> result = new ArrayList<>();
        for (HistoryApplicationEntity item : entities) {
            HistoryApplicationDTO historyApplicationDTO = historyApplicationConverter.toDTO(item);
            result.add(historyApplicationDTO);
        }
        return result;
    }

    /**
     * find history off application on device
     *
     * @param deviceApplicationId
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @Override
    public List<HistoryApplicationDTO> findHistoryAppDeviceLater(Long deviceApplicationId, int day, long hour, int minutes) {
        if (!deviceApplicationRepository.existsById(deviceApplicationId)) {
            throw new ResourceNotFoundException("Not found device-application with id = " + deviceApplicationId);
        }
        List<HistoryApplicationDTO> result = new ArrayList<>();
        List<HistoryApplicationEntity> historyApplicationEntities = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-minutes).plusDays(-day).plusHours(-hour);
        historyApplicationEntities = historyApplicationRepository.findAllByHistoryDeviceApplicationEntityIdAndCreatedDateBetween(deviceApplicationId, time, LocalDateTime.now());
        for (HistoryApplicationEntity iteam : historyApplicationEntities) {
            HistoryApplicationDTO historyApplicationDTO = historyApplicationConverter.toDTO(iteam);
            result.add(historyApplicationDTO);
        }
        return result;
    }

    @Override
    public int totalItem() {
        return (int) historyApplicationRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            historyApplicationRepository.deleteById(item);
        }
    }
}
