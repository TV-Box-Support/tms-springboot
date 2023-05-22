package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DeviceConverter;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.HistoryApplicationEntity;
import com.vnptt.tms.entity.HistoryPerformanceEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.HistoryApplicationRepository;
import com.vnptt.tms.repository.HistoryPerformanceRepository;
import com.vnptt.tms.security.jwt.JwtUtils;
import com.vnptt.tms.security.responce.JwtBoxResponse;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeviceService implements IDeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private HistoryApplicationRepository historyApplicationRepository;

    @Autowired
    private HistoryPerformanceRepository historyPerformanceRepository;

    @Autowired
    private DeviceConverter deviceConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Save device in production path and update device infor for box
     *
     * @param deviceDTO
     * @return
     */
    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        DeviceEntity deviceEntity = new DeviceEntity();
        if (deviceDTO.getId() != null) {
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
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Not found device with id = " + id);
        }
        DeviceEntity entity = deviceRepository.findOneById(id);
        return deviceConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findAll(Pageable pageable) {
        List<DeviceEntity> entities = deviceRepository.findAll(pageable).getContent();
        List<DeviceDTO> result = new ArrayList<>();
        for (DeviceEntity item : entities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findAll() {
        List<DeviceEntity> entities = deviceRepository.findAll();
        List<DeviceDTO> result = new ArrayList<>();
        for (DeviceEntity item : entities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    /**
     * find STB infor by SN for box
     *
     * @param serialnumber
     * @return
     */
    @Override
    public DeviceDTO findOneBySn(String ip, String serialnumber) {
        DeviceEntity entity = deviceRepository.findOneBySn(serialnumber);
        if (entity == null) {
            throw new ResourceNotFoundException("Not found device with Serialnumber = " + serialnumber);
        }
        if (!Objects.equals(entity.getIp(), ip)){
            entity.setIp(ip);
            deviceRepository.save(entity);
        }
        return deviceConverter.toDTO(entity);
    }

    /**
     * find nomal
     *
     * @param model
     * @param firmwareVer
     * @return
     */
    @Override
    public List<DeviceDTO> findByModelAndFirmwareVer(String model, String firmwareVer) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        if (model == null) model = "";
        if (firmwareVer == null) firmwareVer = "";
        deviceEntities = deviceRepository.findAllByModelContainingAndFirmwareVerContaining(model, firmwareVer);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    /**
     * find with pageable
     *
     * @param model
     * @param firmwareVer
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findByModelAndFirmwareVer(String model, String firmwareVer, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        if (model == null) model = "";
        if (firmwareVer == null) firmwareVer = "";
        deviceEntities = deviceRepository.findAllByModelContainingAndFirmwareVerContaining(model, firmwareVer, pageable);
        for (DeviceEntity item : deviceEntities) {
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
        for (DeviceEntity item : deviceEntities) {
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
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    /**
     * find all device with application id
     *
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
        for (DeviceEntity entity : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(entity);
            result.add(deviceDTO);
        }
        return result;
    }

    /**
     * find all device are running 3 minute later to now
     *
     * @return
     */
    @Override
    public List<DeviceDTO> findAllDeviceRunNow() {
        List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
        historyPerformanceEntities = historyPerformanceRepository.findAllByCreatedDateBetween(time, LocalDateTime.now());
        for (HistoryPerformanceEntity iteam : historyPerformanceEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(iteam.getDeviceEntityHistory().getId());
            if (deviceEntity != null && result.stream().noneMatch(device -> device.getId().equals(deviceEntity.getId()))) {
                result.add(deviceConverter.toDTO(deviceEntity));
            }
        }
        return result;
    }

    /**
     * todo: modify
     *
     * @param serialnumber
     * @param mac
     * @return
     */
    @Override
    public ResponseEntity<?> authenticateDevice(String serialnumber, String mac) {
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(serialnumber);
        if (deviceEntity == null) {
            throw new ResourceNotFoundException("not found Box with sn = " + serialnumber);
        }
        if (!Objects.equals(deviceEntity.getMac(), mac)) {
            throw new ResourceNotFoundException("not found Box with sn = " + serialnumber + " mac = " + mac);
        }
        String jwt = jwtUtils.generateJwtTokenBOX(deviceEntity);

        return ResponseEntity.ok(
                new JwtBoxResponse(jwt,
                        deviceEntity.getId(),
                        deviceEntity.getSn(),
                        deviceEntity.getMac(),
                        "ROLE_USER",
                        "BOX"));
    }

    /**
     * find device active with time
     *
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @Override
    public List<DeviceDTO> findDeviceActive(int day, long hour, int minutes) {
        List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-minutes).plusDays(-day).plusHours(-hour);
        historyPerformanceEntities = historyPerformanceRepository.findAllByCreatedDateBetween(time, LocalDateTime.now());
        for (HistoryPerformanceEntity iteam : historyPerformanceEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(iteam.getDeviceEntityHistory().getId());
            if (deviceEntity != null && result.stream().noneMatch(device -> device.getId().equals(deviceEntity.getId()))) {
                result.add(deviceConverter.toDTO(deviceEntity));
            }
        }
        return result;
    }

    /**
     * find all device are running app 3 minute later to now
     *
     * @param id
     * @return
     */
    @Override
    public List<DeviceDTO> findAllDeviceRunApp(Long id) {
        List<HistoryApplicationEntity> historyApplicationEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
        historyApplicationEntities = historyApplicationRepository.findAllByApplicationEntityHistoryIdAndCreatedDateBetween(id, time, LocalDateTime.now());
        for (HistoryApplicationEntity iteam : historyApplicationEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(iteam.getDeviceEntityAppHistory().getId());
            if (deviceEntity != null && result.stream().noneMatch(device -> device.getId().equals(deviceEntity.getId()))) {
                result.add(deviceConverter.toDTO(deviceEntity));
            }
        }
        return result;
    }


    @Override
    public int totalItem() {
        return (int) deviceRepository.count();
    }

    /**
     * too dangerous (only use to test)
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            deviceRepository.deleteById(item);
        }
    }
}