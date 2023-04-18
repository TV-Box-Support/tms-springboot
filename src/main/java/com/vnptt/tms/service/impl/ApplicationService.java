package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.ApplicationConverter;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ApplicationService implements IApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private DeviceRepository deviceRepository;


    @Autowired
    private ApplicationConverter applicationConverter;

    @Override
    public ApplicationDTO save(ApplicationDTO applicationDTO) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        if (applicationDTO.getId() != null){
            Optional<ApplicationEntity> oldApplicationEntity = applicationRepository.findById(applicationDTO.getId());
            applicationEntity = applicationConverter.toEntity(applicationDTO, oldApplicationEntity.orElse(applicationEntity));
        } else {
            applicationEntity = applicationConverter.toEntity(applicationDTO);
        }
        applicationEntity = applicationRepository.save(applicationEntity);
        return applicationConverter.toDTO(applicationEntity);
    }

    @Override
    public ApplicationDTO findOne(Long id) {
        ApplicationEntity entity = applicationRepository.findOneById(id);
        return applicationConverter.toDTO(entity);
    }

    @Override
    public int totalItem() {
        return (int) applicationRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            applicationRepository.deleteById(item);
        }
    }

    @Override
    public List<ApplicationDTO> findAll(Pageable pageable) {
        List<ApplicationEntity> entities = applicationRepository.findAll(pageable).getContent();
        List<ApplicationDTO> result = new ArrayList<>();
        for(ApplicationEntity item : entities){
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findAll() {
        List<ApplicationEntity> entities = applicationRepository.findAll();
        List<ApplicationDTO> result = new ArrayList<>();
        for(ApplicationEntity item : entities){
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<ApplicationEntity> applicationEntities = applicationRepository.findApplicationEntitiesByDeviceEntitiesApplicationId(deviceId);
        List<ApplicationDTO> result = new ArrayList<>();
        for (ApplicationEntity entity: applicationEntities){
            ApplicationDTO applicationDTO = applicationConverter.toDTO(entity);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public ApplicationDTO addAppToDevice(Long deviceId, ApplicationDTO model) {
        ApplicationEntity applicationEntity = deviceRepository.findById(deviceId).map(DeviceApp -> {
            long applicationId = model.getId();

            // tag is existed
            if (applicationId != 0L) {
                ApplicationEntity app = applicationRepository.findById(applicationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Application with id = " + applicationId));
                DeviceApp.addApplication(app);
                deviceRepository.save(DeviceApp);
                return app;
            }

            // add and create new Tag
            ApplicationEntity entity = applicationConverter.toEntity(model);
            DeviceApp.addApplication(entity);
            return applicationRepository.save(entity);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Device with id = " + deviceId));

        return  model;
    }

    @Override
    public  void removeAppOnDevice(Long deviceId, Long applicationId) {
        DeviceEntity deviceEntity = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Device with id = " + deviceId));

        deviceEntity.removeApplication(applicationId);
        deviceRepository.save(deviceEntity);
    }


    @Override
    public List<ApplicationDTO> findByPackagename(String packagename) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();
        applicationRepository.findByPackagenameContaining(packagename).forEach(applicationEntities::add);
        for (ApplicationEntity item: applicationEntities){
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

}
