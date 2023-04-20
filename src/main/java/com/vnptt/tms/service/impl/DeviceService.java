package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DeviceConverter;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService implements IDeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;


    @Autowired
    private DeviceConverter deviceConverter;

    /**
     * Save device in production path and update device infor for box
     * @param deviceDTO
     * @return
     */
    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        DeviceEntity deviceEntity = new DeviceEntity();
        if (deviceDTO.getId() != null){
            Optional<DeviceEntity> oldDeviceEntity = deviceRepository.findById(deviceDTO.getId());
            deviceEntity = deviceConverter.toEntity(deviceDTO, oldDeviceEntity.get());
        } else {
            deviceEntity = deviceConverter.toEntity(deviceDTO);
        }
        deviceEntity = deviceRepository.save(deviceEntity);
        return deviceConverter.toDTO(deviceEntity);
    }

    @Override
    public DeviceDTO findOne(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Not found device with id = " + id);
        }
        DeviceEntity entity = deviceRepository.findOneById(id);
        return deviceConverter.toDTO(entity);
    }

    /**
     *  find item with page number and totalPage number
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findAll(Pageable pageable) {
        List<DeviceEntity> entities = deviceRepository.findAll(pageable).getContent();
        List<DeviceDTO> result = new ArrayList<>();
        for(DeviceEntity item : entities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findAll() {
        List<DeviceEntity> entities = deviceRepository.findAll();
        List<DeviceDTO> result = new ArrayList<>();
        for(DeviceEntity item : entities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public DeviceDTO findOneBySn(String serialnumber) {
        DeviceEntity entity = deviceRepository.findOneBySn(serialnumber);
        if (entity == null) {
            throw new ResourceNotFoundException("Not found device with Serialnumber = " + serialnumber);
        }
        return deviceConverter.toDTO(entity);
    }

    @Override
    public List<DeviceDTO> findByModelAndFirmwareVer(String model, String firmwareVer) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        if (model == null) model = "";
        if (firmwareVer == null) firmwareVer = "";
        deviceEntities = deviceRepository.findAllByModelContainingAndFirmwareVerContaining(model, firmwareVer);
        for (DeviceEntity item: deviceEntities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findByModelAndFirmwareVer(String model, String firmwareVer, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        if (model == null) model = "";
        if (firmwareVer == null) firmwareVer = "";
        deviceEntities = deviceRepository.findAllByModelContainingAndFirmwareVerContaining(model, firmwareVer, pageable);
        for (DeviceEntity item: deviceEntities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findByLocation(String location) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByLocationContaining(location);
        for (DeviceEntity item: deviceEntities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findByDate(Date date) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByDate(date);
        for (DeviceEntity item: deviceEntities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    /**
     * find all device with application id
     * @param applicationId
     * @return
     */
    @Override
    public List<DeviceDTO> findAllWithApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }
        List<DeviceEntity> deviceEntities = deviceRepository.findDeviceEntitiesByApplicationEntitiesId(applicationId);
        List<DeviceDTO> result = new ArrayList<>();
        for (DeviceEntity entity: deviceEntities){
            DeviceDTO deviceDTO = deviceConverter.toDTO(entity);
            result.add(deviceDTO);
        }
        return result;
    }


    @Override
    public int totalItem(){
        return (int) deviceRepository.count();
    }

    /**
     * too dangerous (only use to test)
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            deviceRepository.deleteById(item);
        }
    }
}