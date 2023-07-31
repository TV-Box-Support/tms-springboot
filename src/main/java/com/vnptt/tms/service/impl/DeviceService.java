package com.vnptt.tms.service.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.vnptt.tms.api.output.chart.AreaChart;
import com.vnptt.tms.api.output.chart.BarChart;
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
        List<DeviceEntity> entities = deviceRepository.findAllByOrderByModifiedDateDesc(pageable);
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
            File database = new File("ipdatabase/data.mmdb");

            // This creates the DatabaseReader object. To improve performance, reuse
            // the object across lookups. The object is thread-safe.
            DatabaseReader reader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName(ip);
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

    @Override
    public List<DeviceDTO> findByLocation(String location, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByLocationContainingOrderByModifiedDateDesc(location, pageable);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

//    @Override
//    public List<DeviceDTO> findByDate(Date date, Pageable pageable) {
//        List<DeviceEntity> deviceEntities = new ArrayList<>();
//        List<DeviceDTO> result = new ArrayList<>();
//        deviceEntities = deviceRepository.findAllByDateOrderByModifiedDateDesc(date, pageable);
//        for (DeviceEntity item : deviceEntities) {
//            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
//            result.add(deviceDTO);
//        }
//        return result;
//    }

    /**
     * find all device with application id
     *
     * @param applicationId
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findAllWithApplication(Long applicationId, Pageable pageable) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }
        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findAllByApplicationEntityDetailIdOrderByModifiedDateDesc(applicationId, pageable);
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
    public List<DeviceDTO> findAllDeviceRunNow(Pageable pageable) {
        List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
        historyPerformanceEntities = historyPerformanceRepository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(time, LocalDateTime.now(), pageable);
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
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findDeviceActive(int day, long hour, int minutes, Pageable pageable) {
        List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-minutes).plusDays(-day).plusHours(-hour);
        historyPerformanceEntities = historyPerformanceRepository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(time, LocalDateTime.now(), pageable);
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
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findDeviceInListDevice(Long listDeviceId, Pageable pageable) {
        List<DeviceDTO> result = new ArrayList<>();
        ListDeviceEntity listDevice = listDeviceRepository.findOneById(listDeviceId);
        if (listDevice == null) {
            throw new ResourceNotFoundException("not found list device with Id = " + listDeviceId);
        }
        List<DeviceEntity> deviceEntities = deviceRepository.findAllByListDeviceDetailIdOrderByModifiedDateDesc(listDeviceId, pageable);
        for (DeviceEntity entity : deviceEntities) {
            result.add(deviceConverter.toDTO(entity));
        }
        return result;
    }

    @Override
    public TerminalStudioOutput updateTerminalStudioInfo() {
        TerminalStudioOutput terminalStudioOutput = new TerminalStudioOutput();
        LocalDateTime timeOnline = LocalDateTime.now().plusMinutes(-3);
        LocalDateTime timeLast7day = LocalDateTime.now().plusDays(-7);
        LocalDateTime timeLast30day = LocalDateTime.now().plusDays(-30);
        Long deviceOnline = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeOnline, LocalDateTime.now());
        Long last7day = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeLast7day, LocalDateTime.now());
        Long last30day = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeLast30day, LocalDateTime.now());
        terminalStudioOutput.setOnline(deviceOnline);
        terminalStudioOutput.setLast7day(last7day);
        terminalStudioOutput.setLast30day(last30day);
        terminalStudioOutput.setTotal(deviceRepository.count());
        return terminalStudioOutput;
    }

    @Override
    public List<PieChart> getTotalPieChart(String type) {
        List<PieChart> result = new ArrayList<>();
        if (Objects.equals(type, "online")) {
            Long total = deviceRepository.count();
            LocalDateTime timeOnline = LocalDateTime.now().plusMinutes(-3);
            Long online = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(timeOnline, LocalDateTime.now());
            Long notActive = deviceRepository.countDistinctByHistoryPerformanceEntitiesIsNull();
            Long offline = total - online;
            result.add(new PieChart(online, "Online"));
            result.add(new PieChart(notActive, "Not active"));
            result.add(new PieChart(offline, "Offline"));
        } else if (Objects.equals(type, "hdmi")) {
            Long hdmi480 = deviceRepository.countByHdmiBetween(0, 480);
            Long hdmi720 = deviceRepository.countByHdmiBetween(481, 720);
            Long hdmi1080 = deviceRepository.countByHdmiBetween(721, 1080);
            Long hdmi2k = deviceRepository.countByHdmiBetween(1081, 2000);
            Long hdmi4k = deviceRepository.countByHdmiBetween(2001, 4000);
            result.add(new PieChart(hdmi480, "480P & lower"));
            result.add(new PieChart(hdmi720, "720P"));
            result.add(new PieChart(hdmi1080, "1080P"));
            result.add(new PieChart(hdmi2k, "2K"));
            result.add(new PieChart(hdmi4k, "4K & upper"));
        } else if (Objects.equals(type, "network")) {
            Long wifi = deviceRepository.countByNetworkContaining("Wifi");
            Long ethernet = deviceRepository.countByNetworkContaining("Ethernet");
            Long diff = deviceRepository.countByNetworkContaining("diff");
            result.add(new PieChart(wifi, "Wifi"));
            result.add(new PieChart(ethernet, "4K & Ethernet"));
        }
        return result;
    }

    @Override
    public List<BarChart> getTotalBarChart() {
        List<BarChart> result = new ArrayList<>();
        for (int i = 7; i > 0; i--) {
            LocalDate DaysAgo = LocalDate.now().minusDays(i);
            LocalDateTime start = LocalDateTime.of(DaysAgo, LocalTime.MIN);
            LocalDateTime end;
            if (i == 1) {
                end = LocalDateTime.now();
            } else {
                end = LocalDateTime.of(DaysAgo, LocalTime.MAX);
            }
            Long deviceOnline = deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(start, end);
            result.add(new BarChart(DaysAgo, deviceOnline));
        }
        return result;
    }

    /**
     * update Box info for Box
     *
     * @param sn
     * @param model
     * @return
     */
    @Override
    public DeviceDTO boxUpdate(String sn, DeviceDTO model) {
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(sn);
        if (deviceEntity == null) {
            throw new ResourceNotFoundException("not found device with serialnumber: " + sn);
        }
        deviceEntity = deviceConverter.toEntity(model, deviceEntity);
        deviceEntity = deviceRepository.save(deviceEntity);
        return deviceConverter.toDTO(deviceEntity);
    }

    @Override
    public Long countDeviceRunNow() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
        return deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(time, LocalDateTime.now());
    }

    @Override
    public Long countDeviceActive(int day, long hour, int minutes) {
        LocalDateTime time = LocalDateTime.now().plusMinutes(-minutes).plusDays(-day).plusHours(-hour);
        return deviceRepository.countDistinctByHistoryPerformanceEntitiesCreatedDateBetween(time, LocalDateTime.now());
    }

    @Override
    public List<DeviceDTO> findByDescriptionAndSn(String search, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByDescriptionContainingOrSnContainingOrderByModifiedDateDesc(search, search, pageable);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public Long countByDescriptionAndSn(String description) {
        return deviceRepository.countByDescriptionContainingOrSnContaining(description, description);
    }

    @Override
    public List<DeviceDTO> findByDescriptionAndLocation(String location, String description, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByLocationContainingOrDescriptionContainingOrderByModifiedDateDesc(location, description, pageable);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public Long countByDescriptionAndLocation(String location, String description) {
        return deviceRepository.countByLocationContainingOrDescriptionContaining(location, description);
    }

    @Override
    public Long countByLocation(String location) {
        return deviceRepository.countByLocationContaining(location);
    }

    @Override
    public List<AreaChart> getAreaChartStatus(Integer dayAgo, Long id) {
        List<AreaChart> result = new ArrayList<>();

        LocalDate DaysAgo = LocalDate.now().minusDays(dayAgo);
        LocalDateTime start = LocalDateTime.of(DaysAgo, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(DaysAgo, LocalTime.MAX);
        List<HistoryPerformanceEntity> historyPerformanceEntity = historyPerformanceRepository.findAllByDeviceEntityHistoryIdAndAndCreatedDateBetweenOrderByCreatedDateAsc(id, start, end);
        if (historyPerformanceEntity == null) {
            throw new ResourceNotFoundException("device not active in " + dayAgo + " day age");
        }

        for (int i = 0; i < 480; i++) {
            HistoryPerformanceEntity entity = new HistoryPerformanceEntity();
            if (historyPerformanceEntity.size() != 0) {
                entity = historyPerformanceEntity.get(1);
            } else {
                AreaChart areaChart = new AreaChart(start.plusMinutes(i * 3), 0.0, 0.0);
                result.add(areaChart);
                continue;
            }

            if (entity.getCreatedDate().plusMinutes(-1).plusSeconds(-30).isBefore(start.plusMinutes(i * 3))
                    && entity.getCreatedDate().plusMinutes(1).plusSeconds(30).isAfter(start.plusMinutes(i * 3))) {
                AreaChart areaChart = new AreaChart(start.plusMinutes(i * 3), entity.getCpu(), entity.getMemory());
                result.add(areaChart);
                historyPerformanceEntity.remove(1);
            } else {
                AreaChart areaChart = new AreaChart(start.plusMinutes(i * 3), 0.0, 0.0);
                result.add(areaChart);
            }
        }

        return result;
    }

    /**
     * find all device has policy
     *
     * @param policyId
     * @param pageable
     * @return
     */
    @Override
    public List<DeviceDTO> findDeviceWithPolicyId(Long policyId, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByDevicePolicyDetailEntitiesPolicyEntityDetailIdOrderByModifiedDateDesc(policyId, pageable);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    /**
     * count all device has policy
     *
     * @param policyId
     * @return
     */
    @Override
    public Long countDeviceWithPolicyId(Long policyId) {
        return deviceRepository.countByDevicePolicyDetailEntitiesPolicyEntityDetailId(policyId);
    }

    @Override
    public List<DeviceDTO> findAllDeviceRunNowWithSN(String serialmunber, Pageable pageable) {
        List<HistoryPerformanceEntity> historyPerformanceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
        historyPerformanceEntities = historyPerformanceRepository.findAllByDeviceEntityHistorySnContainingAndCreatedDateBetweenOrderByCreatedDateDesc(serialmunber, time, LocalDateTime.now(), pageable);
        for (HistoryPerformanceEntity iteam : historyPerformanceEntities) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(iteam.getDeviceEntityHistory().getId());
            if (deviceEntity != null && result.stream().noneMatch(device -> device.getId().equals(deviceEntity.getId()))) {
                result.add(deviceConverter.toDTO(deviceEntity));
            }
        }
        return result;
    }

    @Override
    public Long countDeviceRunNowWithSN(String serialmunber) {
        LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
        return deviceRepository.countDistinctBySnContainingAndHistoryPerformanceEntitiesCreatedDateBetween(serialmunber, time, LocalDateTime.now());
    }

    @Override
    public Long countDeviceWithPolicyIdAndSn(Long policyId, String sn) {
        return deviceRepository.countByDevicePolicyDetailEntitiesPolicyEntityDetailIdAndSnContaining(policyId, sn);
    }

    @Override
    public List<DeviceDTO> findDeviceWithPolicyIdAndSN(Long policyId, String sn, Pageable pageable) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findAllByDevicePolicyDetailEntitiesPolicyEntityDetailIdAndSnContainingOrderByModifiedDateDesc(policyId, sn, pageable);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findAllWithApplicationIdAndSn(Long applicationId, String sn, Pageable pageable) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Not found application with id = " + applicationId);
        }
        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findAllByApplicationEntityDetailIdAndDeviceAppEntityDetailSnContainingOrderByModifiedDateDesc(applicationId, sn, pageable);
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

    @Override
    public Long countByApplicationIdAndSn(Long applicationId, String sn) {
        return deviceApplicationRepository.countAllByApplicationEntityDetailIdAndDeviceAppEntityDetailSnContaining(applicationId, sn);
    }

    @Override
    public Long countByApplicationId(Long applicationId) {
        return deviceApplicationRepository.countAllByApplicationEntityDetailId(applicationId);
    }

    @Override
    public List<DeviceDTO> findOnBarSearch(String search) {
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceDTO> result = new ArrayList<>();
        deviceEntities = deviceRepository.findTop5BySnContainingOrderByModifiedDateDesc(search);
        for (DeviceEntity item : deviceEntities) {
            DeviceDTO deviceDTO = deviceConverter.toDTO(item);
            result.add(deviceDTO);
        }
        return result;
    }

    @Override
    public List<DeviceDTO> findDeviceInListDeviceWithSn(Long listDeviceId, String serialmunber, Pageable pageable) {
        List<DeviceDTO> result = new ArrayList<>();
        ListDeviceEntity listDevice = listDeviceRepository.findOneById(listDeviceId);
        if (listDevice == null) {
            throw new ResourceNotFoundException("not found list device with Id = " + listDeviceId);
        }
        List<DeviceEntity> deviceEntities = deviceRepository.findAllByListDeviceDetailIdAndSnContainingOrderByModifiedDateDesc(listDeviceId, serialmunber, pageable);
        for (DeviceEntity entity : deviceEntities) {
            result.add(deviceConverter.toDTO(entity));
        }
        return result;
    }

    @Override
    public Long countDeviceinListDeviceWithSn(Long listDeviceId, String serialmunber) {
        return deviceRepository.countByListDeviceDetailIdAndSnContaining(listDeviceId, serialmunber);
    }

    @Override
    public Long countDeviceinListDevice(Long listDeviceId) {
        return deviceRepository.countAllByListDeviceDetailId(listDeviceId);
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