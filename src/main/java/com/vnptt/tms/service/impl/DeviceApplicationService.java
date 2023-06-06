package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DeviceApplicationConverter;
import com.vnptt.tms.dto.DeviceApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.DeviceApplicationEntity;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.exception.FileStorageException;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceApplicationRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.service.IDeviceApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DeviceApplicationService implements IDeviceApplicationService {

    @Autowired
    private DeviceApplicationRepository deviceApplicationRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private DeviceApplicationConverter deviceApplicationConverter;


    /**
     * unnecessary (only use to test)
     * save to database when post and put app
     *
     * @return
     */
    @Override
    public DeviceApplicationDTO save(Long deviceId, Long applicationId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }

        DeviceApplicationEntity deviceApplicationEntity = deviceApplicationRepository.findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(deviceId, applicationId);
        if (deviceApplicationEntity != null) {
            throw new FileStorageException("device exists application");
        }

        DeviceEntity deviceEntity = deviceRepository.findOneById(deviceId);
        ApplicationEntity applicationEntity = applicationRepository.findOneById(applicationId);

        deviceApplicationEntity.setIsalive(true);
        deviceApplicationEntity.setDeviceAppEntityDetail(deviceEntity);
        deviceApplicationEntity.setApplicationEntityDetail(applicationEntity);
        deviceApplicationEntity = deviceApplicationRepository.save(deviceApplicationEntity);
        return deviceApplicationConverter.toDTO(deviceApplicationEntity);
    }

    /**
     * find app with id
     *
     * @param id
     * @return
     */
    @Override
    public DeviceApplicationDTO findOne(Long id) {
        DeviceApplicationEntity entity = deviceApplicationRepository.findOneById(id);
        return deviceApplicationConverter.toDTO(entity);
    }

    /**
     * total item app on database
     *
     * @return
     */
    @Override
    public int totalItem() {
        return (int) deviceApplicationRepository.count();
    }

    /**
     * Delete app on database
     *
     * @param ids list id app
     */
    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            deviceApplicationRepository.deleteById(item);
        }
    }

    /**
     * find all app with pageable
     *
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceApplicationDTO> findAll(Pageable pageable) {
        List<DeviceApplicationEntity> entities = deviceApplicationRepository.findAll(pageable).getContent();
        List<DeviceApplicationDTO> result = new ArrayList<>();
        for (DeviceApplicationEntity item : entities) {
            DeviceApplicationDTO deviceApplicationDTO = deviceApplicationConverter.toDTO(item);
            result.add(deviceApplicationDTO);
        }
        return result;
    }

    /**
     * find all app nomal
     *
     * @return
     */
    @Override
    public List<DeviceApplicationDTO> findAll() {
        List<DeviceApplicationEntity> entities = deviceApplicationRepository.findAll();
        List<DeviceApplicationDTO> result = new ArrayList<>();
        for (DeviceApplicationEntity item : entities) {
            DeviceApplicationDTO deviceApplicationDTO = deviceApplicationConverter.toDTO(item);
            result.add(deviceApplicationDTO);
        }
        return result;
    }

    @Override
    public void removeAppOnDevice(Long deviceId, Long applicationId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }
        DeviceApplicationEntity deviceApplication = deviceApplicationRepository.findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(deviceId, applicationId);
        if (deviceApplication == null) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId + " in device with id = " + deviceId);
        }

        deviceApplication.setIsalive(false);
        deviceApplicationRepository.save(deviceApplication);
    }

    @Override
    public DeviceApplicationDTO update(Long deviceId, Long applicationId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }
        DeviceApplicationEntity deviceApplication = deviceApplicationRepository.findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(deviceId, applicationId);
        if (deviceApplication == null) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId + " in device with id = " + deviceId);
        }

        deviceApplication.setIsalive(true);
        deviceApplicationRepository.save(deviceApplication);

        return deviceApplicationConverter.toDTO(deviceApplication);
    }

    @Override
    public List<DeviceApplicationDTO> findAllWithName(String name, Pageable pageable) {
        List<DeviceApplicationEntity> entities = deviceApplicationRepository.findAllByApplicationEntityDetailNameOrderByModifiedDateDesc(name, pageable);
        List<DeviceApplicationDTO> result = new ArrayList<>();
        for (DeviceApplicationEntity item : entities) {
            DeviceApplicationDTO deviceApplicationDTO = deviceApplicationConverter.toDTO(item);
            result.add(deviceApplicationDTO);
        }
        return result;
    }

    @Override
    public Long countAllWithName(String name) {
        return deviceApplicationRepository.countByApplicationEntityDetailNameContaining(name);
    }

}
