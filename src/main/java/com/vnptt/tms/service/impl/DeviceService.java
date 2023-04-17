package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DeviceConverter;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService implements IDeviceService {

    @Autowired
    private DeviceRepository deviceRepository;


    @Autowired
    private DeviceConverter deviceConverter;

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
    public int totalItem(){
        return (int) deviceRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            deviceRepository.deleteById(item);
        }
    }
}