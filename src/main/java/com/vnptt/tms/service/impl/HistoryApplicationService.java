package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.HistoryApplicationConverter;
import com.vnptt.tms.dto.HistoryApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.HistoryApplicationEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.HistoryApplicationRepository;
import com.vnptt.tms.service.IHistoryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class HistoryApplicationService implements IHistoryApplicationService {

    @Autowired
    private HistoryApplicationRepository historyApplicationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private HistoryApplicationConverter historyApplicationConverter;

    /**
     * save history application, if application is still in device
     *
     * @param historyApplicationDTO
     * @return
     */
    @Override
    public HistoryApplicationDTO save(HistoryApplicationDTO historyApplicationDTO) {
        HistoryApplicationEntity historyApplicationEntity = historyApplicationConverter.toEntity(historyApplicationDTO);

        DeviceEntity deviceEntity = deviceRepository.findOneById(historyApplicationDTO.getDeviceId());
        historyApplicationEntity.setDeviceEntityAppHistory(deviceEntity);

        List<ApplicationEntity> applicationEntities = deviceEntity.getApplicationEntities();
        ApplicationEntity applicationDevice = new ApplicationEntity();

        //check app already exists in the listApp of device
        for (ApplicationEntity iteam : applicationEntities) {
            if (Objects.equals(iteam.getId(), historyApplicationDTO.getApplicationId())) {
                applicationDevice = iteam;
            }
        }

        if (applicationDevice.getPackagename() == null) {
            throw new ResourceNotFoundException("not found application infor with id " + historyApplicationDTO.getApplicationId() + " in device, must map application to device");
        }

        historyApplicationEntity.setApplicationEntityHistory(applicationDevice);
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
     * @param deviceId
     * @param applicationId
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @Override
    public List<HistoryApplicationDTO> findHistoryAppDeviceLater(Long deviceId, Long applicationId, int day, long hour, int minutes) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }
        List<HistoryApplicationDTO> result = new ArrayList<>();
        List<HistoryApplicationEntity> historyApplicationEntities = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-minutes).plusDays(-day).plusHours(-hour);
        historyApplicationEntities = historyApplicationRepository.findAllByDeviceEntityAppHistoryIdAndApplicationEntityHistoryIdAndCreatedDateBetween(deviceId, applicationId, time, LocalDateTime.now());
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
