package com.vnptt.tms.service.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.vnptt.tms.api.output.chart.AreaChart;
import com.vnptt.tms.api.output.chart.PieChart;
import com.vnptt.tms.api.output.studio.TerminalStudioOutput;
import com.vnptt.tms.converter.DeviceConverter;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.entity.*;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.*;
import com.vnptt.tms.security.jwt.JwtUtils;
import com.vnptt.tms.security.responce.JwtBoxResponse;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.InetAddress;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private DeviceApplicationRepository deviceApplicationRepository;

    @Autowired
    private HistoryApplicationRepository historyApplicationRepository;

    @Autowired
    private HistoryPerformanceRepository historyPerformanceRepository;

    @Autowired
    private ListDeviceRepository listDeviceRepository;

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
            deviceEntity = deviceRepository.save(deviceEntity);
        } else {
            deviceEntity = deviceConverter.toEntity(deviceDTO);
            deviceEntity = deviceRepository.save(deviceEntity);
            // add all device to list "all"
            ListDeviceEntity listDeviceEntity = listDeviceRepository.findOneByName("all");
            if (listDeviceEntity == null) {
                throw new ResourceNotFoundException("miss list device all device!");
            }
            listDeviceEntity.addDevice(deviceEntity);
            listDeviceRepository.save(listDeviceEntity);
        }
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

        try {
            // A File object pointing to file*.mmdb database
            File database = new File("src/main/resources/data.mmdb");

            // This creates the DatabaseReader object. To improve performance, reuse
            // the object across lookups. The object is thread-safe.
            DatabaseReader reader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName("14.224.147.23");
            CityResponse response = reader.city(ipAddress);

            City city = response.getCity();
            if (!Objects.equals(city.getName(), entity.getLocation())) {
                entity.setLocation(city.getName());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (!Objects.equals(entity.getIp(), ip)) {
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
        deviceEntities = deviceRepository.findAllByModelContainingAndFirmwareVerContainingOrderByModifiedDateDesc(model, firmwareVer);
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
        deviceEntities = deviceRepository.findAllByModelContainingAndFirmwareVerContainingOrderByModifiedDateDesc(model, firmwareVer, pageable);
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
        deviceEntities = deviceRepository.findAllByLocationContainingOrderByModifiedDateDesc(location);
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
        deviceEntities = deviceRepository.findAllByDateOrderByModifiedDateDesc(date);
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
        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findAllByApplicationEntityDetailIdOrderByModifiedDateDesc(applicationId);
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        for (DeviceApplicationEntity item : deviceApplicationEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(item.getDeviceAppEntityDetail().getId());
            if (deviceEntity != null) {
                deviceEntities.add(deviceEntity);
            }
        }
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
        historyPerformanceEntities = historyPerformanceRepository.findAllByCreatedDateBetweenOrderByModifiedDateDesc(time, LocalDateTime.now());
        for (HistoryPerformanceEntity iteam : historyPerformanceEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(iteam.getDeviceEntityHistory().getId());
            if (deviceEntity != null && result.stream().noneMatch(device -> device.getId().equals(deviceEntity.getId()))) {
                result.add(deviceConverter.toDTO(deviceEntity));
            }
        }
        return result;
    }

    /**
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
        historyPerformanceEntities = historyPerformanceRepository.findAllByCreatedDateBetweenOrderByModifiedDateDesc(time, LocalDateTime.now());
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
     * @param applicationId
     * @return
     */
    @Override
    public List<DeviceDTO> findAllDeviceRunApp(Long applicationId) {
        List<HistoryApplicationEntity> historyApplicationEntities = new ArrayList<>();
        List<DeviceApplicationEntity> deviceApplicationEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);

        historyApplicationEntities = historyApplicationRepository.findAllByCreatedDateBetween(time, LocalDateTime.now());
        for (HistoryApplicationEntity iteam : historyApplicationEntities) {
            DeviceApplicationEntity deviceApplicationEntity = deviceApplicationRepository.findOneById(iteam.getHistoryDeviceApplicationEntity().getId());
            if (deviceApplicationEntity != null && result.stream().noneMatch(deviceApplication -> deviceApplication.getId().equals(deviceApplicationEntity.getId()))) {
                deviceApplicationEntities.add(deviceApplicationEntity);
            }
        }
        for (DeviceApplicationEntity iteam : deviceApplicationEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(iteam.getDeviceAppEntityDetail().getId());
            if (deviceEntity != null && result.stream().noneMatch(device -> device.getId().equals(deviceEntity.getId()))) {
                result.add(deviceConverter.toDTO(deviceEntity));
            }
        }
        return result;
    }

    /**
     * add device to list
     *
     * @param listDeviceId
     * @param deviceIds
     * @return
     */
    @Override
    public List<DeviceDTO> mapDeviceToListDevice(Long listDeviceId, Long[] deviceIds) {
        List<DeviceDTO> result = new ArrayList<>();
        for (Long deviceId : deviceIds) {
            DeviceEntity deviceEntity = listDeviceRepository.findById(listDeviceId).map(listDevice -> {
                DeviceEntity device = deviceRepository.findById(deviceId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found device with id = " + deviceId));

                // check if apk has still
                List<DeviceEntity> deviceEntities = listDevice.getListDeviceDetail();
                for (DeviceEntity item : deviceEntities) {
                    if (item.equals(device)) {
                        return device;
                    }
                }
                //map and add apk to policy
                listDevice.addDevice(device);
                listDeviceRepository.save(listDevice);
                return device;
            }).orElseThrow(() -> new ResourceNotFoundException("Not found listDeavice with id = " + listDeviceId));

            result.add(deviceConverter.toDTO(deviceEntity));
        }
        return result;
    }

    /**
     * remove device out of list
     *
     * @param listDeviceId
     * @param deviceId
     */
    @Override
    public void removeDeviceinListDevice(Long listDeviceId, Long deviceId) {
        ListDeviceEntity listDeviceEntity = listDeviceRepository.findById(listDeviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found list Device with id = " + listDeviceId));

        List<DeviceEntity> deviceEntities = listDeviceEntity.getListDeviceDetail();
        boolean remove = false;
        for (DeviceEntity entity : deviceEntities) {
            if (Objects.equals(entity.getId(), deviceId)) {
                remove = true;
            }
        }
        if (remove) {
            listDeviceEntity.removeDevice(deviceId);
            listDeviceRepository.save(listDeviceEntity);
        } else {
            throw new ResourceNotFoundException("policy don't have device with id = " + deviceId);
        }
    }

    /**
     * find all device in list
     *
     * @param listDeviceId id of list device
     * @return
     */
    @Override
    public List<DeviceDTO> findDeviceInListDevice(Long listDeviceId) {
        List<DeviceDTO> result = new ArrayList<>();
        ListDeviceEntity listDevice = listDeviceRepository.findOneById(listDeviceId);
        if (listDevice == null) {
            throw new ResourceNotFoundException("not found list device with Id = " + listDeviceId);
        }
        List<DeviceEntity> deviceEntities = listDevice.getListDeviceDetail();
        for (DeviceEntity entity : deviceEntities) {
            result.add(deviceConverter.toDTO(entity));
        }
        return result;
    }

    @Override
    public TerminalStudioOutput updateTerminalStudioInfo() {
        TerminalStudioOutput terminalStudioOutput = new TerminalStudioOutput();
        LocalDateTime timeOnline = LocalDateTime.now().plusMinutes(-3);
        LocalDateTime timeLast7day = LocalDateTime.now().plusDays(7);
        LocalDateTime timeLast30day = LocalDateTime.now().plusDays(30);
        Long deviceOnline = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeOnline, LocalDateTime.now());
        Long last7day = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeLast7day, LocalDateTime.now());
        Long last30day = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeLast30day, LocalDateTime.now());
        terminalStudioOutput.setOnline(deviceOnline);
        terminalStudioOutput.setLast7day(deviceOnline);
        terminalStudioOutput.setLast30day(last30day);
        terminalStudioOutput.setTotal(deviceRepository.count());
        return terminalStudioOutput;
    }

    @Override
    public List<PieChart> getTotalPieChart() {
        List<PieChart> result = new ArrayList<>();
        Long total = deviceRepository.count();
        LocalDateTime timeOnline = LocalDateTime.now().plusMinutes(-3);
        Long online = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeOnline, LocalDateTime.now());
        Long notActive = deviceRepository.countDistinctByHistoryPerformanceEntitiesIsNull();
        Long offline = total - online;
        result.add(new PieChart(online,"online"));
        result.add(new PieChart(notActive,"notActive"));
        result.add(new PieChart(offline,"offline"));
        return result;
    }

    @Override
    public List<AreaChart> getTotalAreaChart() {
        List<AreaChart> result = new ArrayList<>();
        for (int i = 7; i > 0; i--) {
            LocalDate DaysAgo = LocalDate.now().minusDays(i);
            LocalDateTime start = LocalDateTime.of(DaysAgo, LocalTime.MIN);
            LocalDateTime end;
            if (i == 7) {
                end = LocalDateTime.now();
            } else {
                end = LocalDateTime.of(DaysAgo, LocalTime.MAX);
            }
            Long deviceOnline = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(start, end);
            result.add(new AreaChart(DaysAgo, deviceOnline));
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