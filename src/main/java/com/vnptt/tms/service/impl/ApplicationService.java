package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.ApplicationConverter;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.DeviceApplicationEntity;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.exception.FileStorageException;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceApplicationRepository;
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
    private DeviceApplicationRepository deviceApplicationRepository;


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
     * add app to device if app not found create new app
     *
     * @param deviceId
     * @param model
     * @return
     */
    @Override
    public ApplicationDTO addAppToDevice(Long deviceId, ApplicationDTO model) {

        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        ApplicationEntity applicationEntity = applicationRepository.findOneByPackagenameAndVersion(model.getPackagename(), model.getVersion())
        if (applicationEntity != null) {
            DeviceApplicationEntity deviceApplicationEntity = deviceApplicationRepository.findDeviceApplicationEntityByDeviceAppEntityDetailIdAndApplicationEntityDetailId(deviceId, applicationEntity.getId());
            if (deviceApplicationEntity != null){
                throw new FileStorageException(" Application had map in device ");
            }
            deviceApplicationEntity = new DeviceApplicationEntity();
            deviceApplicationEntity.setDeviceAppEntityDetail(deviceRepository.findOneById(deviceId));
            deviceApplicationEntity.setApplicationEntityDetail(applicationEntity);
            deviceApplicationEntity.setIsalive(true);
            deviceApplicationRepository.save(deviceApplicationEntity);
        } else {
            applicationEntity = applicationConverter.toEntity(model);
        }

        return applicationConverter.toDTO(applicationEntity);
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

    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();

        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findByDeviceAppEntityDetailId(deviceId);
        for (DeviceApplicationEntity item : deviceApplicationEntities) {
            applicationEntities.add(applicationRepository.findOneById(item.getApplicationEntityDetail().getId()));
        }
        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId, String name) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();

        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findByDeviceAppEntityDetailId(deviceId);
        for (DeviceApplicationEntity item : deviceApplicationEntities) {
            applicationEntities.add(applicationRepository.findOneById(item.getApplicationEntityDetail().getId()));
        }
        for (ApplicationEntity item : applicationEntities) {
            if (item.getName().contains(name)) {
                ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
                result.add(applicationDTO);
            }
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId, Boolean isSystem) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();

        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findByDeviceAppEntityDetailIdAndIsalive(deviceId, isSystem);
        for (DeviceApplicationEntity item : deviceApplicationEntities) {
            applicationEntities.add(applicationRepository.findOneById(item.getApplicationEntityDetail().getId()));
        }
        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId, String name, Boolean isSystem) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();

        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findByDeviceAppEntityDetailIdAndIsalive(deviceId, isSystem);
        for (DeviceApplicationEntity item : deviceApplicationEntities) {
            applicationEntities.add(applicationRepository.findOneById(item.getApplicationEntityDetail().getId()));
        }
        for (ApplicationEntity item : applicationEntities) {
            if (item.getName().contains(name)) {
                ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
                result.add(applicationDTO);
            }
        }
        return result;
    }
}
