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

    /**
     * unnecessary (only use to test)
     * save to database when post and put app
     *
     * @param applicationDTO
     * @return
     */
    @Override
    public ApplicationDTO save(ApplicationDTO applicationDTO) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        if (applicationDTO.getId() != null) {
            Optional<ApplicationEntity> oldApplicationEntity = applicationRepository.findById(applicationDTO.getId());
            applicationEntity = applicationConverter.toEntity(applicationDTO, oldApplicationEntity.orElse(applicationEntity));
        } else {
            applicationEntity = applicationConverter.toEntity(applicationDTO);
        }
        applicationEntity = applicationRepository.save(applicationEntity);
        return applicationConverter.toDTO(applicationEntity);
    }

    /**
     * find app with id
     *
     * @param id
     * @return
     */
    @Override
    public ApplicationDTO findOne(Long id) {
        ApplicationEntity entity = applicationRepository.findOneById(id);
        return applicationConverter.toDTO(entity);
    }

    /**
     * total item app on database
     *
     * @return
     */
    @Override
    public int totalItem() {
        return (int) applicationRepository.count();
    }

    /**
     * Delete app on database
     *
     * @param ids list id app
     */
    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            applicationRepository.deleteById(item);
        }
    }

    /**
     * find all app with pageable
     *
     * @param pageable
     * @return
     */
    @Override
    public List<ApplicationDTO> findAll(Pageable pageable) {
        List<ApplicationEntity> entities = applicationRepository.findAll(pageable).getContent();
        List<ApplicationDTO> result = new ArrayList<>();
        for (ApplicationEntity item : entities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    /**
     * find all app nomal
     *
     * @return
     */
    @Override
    public List<ApplicationDTO> findAll() {
        List<ApplicationEntity> entities = applicationRepository.findAll();
        List<ApplicationDTO> result = new ArrayList<>();
        for (ApplicationEntity item : entities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    /**
     * find all app on a device
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<ApplicationEntity> applicationEntities = applicationRepository.findApplicationEntitiesByDeviceEntitiesApplicationId(deviceId);
        List<ApplicationDTO> result = new ArrayList<>();
        for (ApplicationEntity entity : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(entity);
            result.add(applicationDTO);
        }
        return result;
    }

    /**
     * add app to device if app not found create new app
     *
     * @param deviceId
     * @param model
     * @return
     */
    @Override
    public ApplicationDTO addAppToDevice(Long deviceId, ApplicationDTO model) {

        ApplicationEntity applicationEntity = deviceRepository.findById(deviceId).map(Device -> {
            String packagename = model.getPackagename();
            int version = model.getVersion();
            ApplicationEntity app = applicationRepository.findByPackagenameAndVersion(packagename, version);
            if (app != null) {
                //check if exist
                List<ApplicationEntity> applicationEntities = Device.getApplicationEntities();
                for (ApplicationEntity item : applicationEntities) {
                    if (item.equals(app)) {
                        return app;
                    }
                }
                //add
                Device.addApplication(app);
                deviceRepository.save(Device);
            } else {
                // add and create new App
                ApplicationEntity entity = applicationConverter.toEntity(model);
                entity = applicationRepository.save(entity);
                Device.addApplication(entity);
                return entity;
            }

            return app;
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Device with id = " + deviceId));

        return applicationConverter.toDTO(applicationEntity);
    }

    /**
     * remove app on device in database
     *
     * @param deviceId
     * @param applicationId
     */
    @Override
    public void removeAppOnDevice(Long deviceId, Long applicationId) {
        DeviceEntity deviceEntity = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Device with id = " + deviceId));

        deviceEntity.removeApplication(applicationId);
        deviceRepository.save(deviceEntity);
    }

    /**
     * find by package name for web
     *
     * @param packagename
     * @return
     */
    @Override
    public List<ApplicationDTO> findByPackagename(String packagename) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();
        applicationRepository.findByPackagenameContaining(packagename).forEach(applicationEntities::add);
        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findByPackagename(String packagename, Pageable pageable) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();
        applicationRepository.findByPackagenameContaining(packagename, pageable).forEach(applicationEntities::add);
        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }
}
