package com.vnptt.tms.service.impl;

import com.vnptt.tms.api.output.chart.AreaChartHisPerf;
import com.vnptt.tms.api.output.chart.DoubleBarChart;
import com.vnptt.tms.api.output.chart.PieChart;
import com.vnptt.tms.converter.ApplicationConverter;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.*;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.repository.DeviceApplicationRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.HistoryApplicationRepository;
import com.vnptt.tms.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private HistoryApplicationRepository historyApplicationRepository;


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
     * @param model
     * @return
     */
    @Override
    public ApplicationDTO addAppToDevice(String sn, ApplicationDTO model) {
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(sn);
        if (deviceEntity == null) {
            throw new ResourceNotFoundException("Not found device with sn = " + sn);
        }
        ApplicationEntity applicationEntity = applicationRepository.findOneByPackagenameAndVersion(model.getPackagename(), model.getVersion());
        // exist application
        if (applicationEntity != null) {
            DeviceApplicationEntity deviceApplicationEntity = deviceApplicationRepository.findDeviceApplicationEntityByDeviceAppEntityDetailSnAndApplicationEntityDetailId(sn, applicationEntity.getId());
            if (deviceApplicationEntity != null) {
                return applicationConverter.toDTO(applicationEntity);
            }
            //map application to device
            deviceApplicationEntity = new DeviceApplicationEntity();
            deviceApplicationEntity.setDeviceAppEntityDetail(deviceEntity);
            deviceApplicationEntity.setApplicationEntityDetail(applicationEntity);
            deviceApplicationEntity.setIsalive(true);
            deviceApplicationRepository.save(deviceApplicationEntity);
        } else {
            // add applicaion
            applicationEntity = applicationConverter.toEntity(model);
            applicationRepository.save(applicationEntity);

            //map application to device
            DeviceApplicationEntity deviceApplicationEntity = new DeviceApplicationEntity();
            deviceApplicationEntity.setDeviceAppEntityDetail(deviceEntity);
            deviceApplicationEntity.setApplicationEntityDetail(applicationEntity);
            deviceApplicationEntity.setIsalive(true);
            deviceApplicationRepository.save(deviceApplicationEntity);

        }

        return applicationConverter.toDTO(applicationEntity);
    }

    @Override
    public List<ApplicationDTO> findAllOnDevice(Long deviceId, Pageable pageable) {
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<ApplicationDTO> result = new ArrayList<>();

        List<DeviceApplicationEntity> deviceApplicationEntities = deviceApplicationRepository.findByDeviceAppEntityDetailIdOrderByModifiedDateDesc(deviceId, pageable);
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
    public Long countByDeviceId(Long deviceId) {
        return deviceApplicationRepository.countByDeviceAppEntityDetailId(deviceId);
    }

    @Override
    public List<ApplicationDTO> findAllWithDeviceNameIsSystem(Long deviceId, String name, Boolean isAlive, Boolean system, Pageable pageable) {
        List<ApplicationDTO> result = new ArrayList<>();
        List<ApplicationEntity> applicationEntityList = applicationRepository.findByDeviceApplicationEntitiesDeviceAppEntityDetailIdAndDeviceApplicationEntitiesIsaliveAndNameContainingAndIssystemOrderByCreatedDateDesc(deviceId, isAlive, name, system, pageable);
        for (ApplicationEntity item : applicationEntityList) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public Long countWithDeviceNameIsSystem(Long deviceId, String name, Boolean isalive, Boolean isSystem) {
        return deviceApplicationRepository.countByDeviceAppEntityDetailIdAndIsaliveAndApplicationEntityDetailNameContainingAndApplicationEntityDetailIssystem(deviceId, isalive, name, isSystem);
    }

    @Override
    public Long countByPackagename(String packagename) {
        return applicationRepository.countByPackagenameContaining(packagename);
    }

    @Override
    public List<ApplicationDTO> findAllApplicationAliveOnBox(String sn) {
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(sn);
        if (deviceEntity == null) {
            throw new ResourceNotFoundException("can not found device with serialnumber on box ");
        }
        List<ApplicationEntity> applicationEntities = applicationRepository.findByDeviceApplicationEntitiesDeviceAppEntityDetailSnAndDeviceApplicationEntitiesIsalive(sn, true);
        List<ApplicationDTO> result = new ArrayList<>();

        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<DoubleBarChart> getBarChartApplicationDowload() {
        List<DoubleBarChart> result = new ArrayList<>();
        List<ApplicationEntity> list = applicationRepository.findAllApplicationEntitiesByIssystem(false);
        List<ApplicationEntity> top4 = new ArrayList<>();

        if (list.size() > 4) {
            for (int i = 0; i < 4; i++) {
                ApplicationEntity entity = list.get(0);
                for (ApplicationEntity applicationEntity : list) {
                    if (applicationEntity.getDeviceApplicationEntities().size() > entity.getDeviceApplicationEntities().size()) {
                        entity = applicationEntity;
                    }
                }
                top4.add(entity);
                list.remove(entity);
            }
        } else {
            top4 = list;
        }

        for (ApplicationEntity entity : top4) {
            LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
            Long number = deviceApplicationRepository.countByApplicationEntityDetailId(entity.getId());
            Long numberActiveNow = deviceRepository.countDistinctByDeviceApplicationEntitiesApplicationEntityDetailIdAndDeviceApplicationEntitiesHistoryApplicationEntitiesDetailCreatedDateBetweenAndDeviceApplicationEntitiesHistoryApplicationEntitiesDetailMain(entity.getId(), time, LocalDateTime.now(), true);
            String name = entity.getName() + " - " + entity.getVersion();
            //Long numberActiveNow = deviceApplicationRepository.countByApplicationEntityDetailIdAndHistoryApplicationEntitiesDetailCreatedDateBetweenAndHistoryApplicationEntitiesDetailMain(entity.getId(), time, LocalDateTime.now(), true);
            result.add(new DoubleBarChart(name, number - numberActiveNow, numberActiveNow));
        }
        return result;
    }

    @Override
    public List<PieChart> getPieChartApplicationDowload(Long applicationId, String type) {
        List<PieChart> result = new ArrayList<>();
        if (Objects.equals(type, "install")) {
            Long install = deviceApplicationRepository.countByApplicationEntityDetailId(applicationId);
            Long all = deviceRepository.count();
            result.add(new PieChart(all - install, "Not Install"));
            result.add(new PieChart(install, " Install"));
        } else if (Objects.equals(type, "active")) {
            LocalDateTime time = LocalDateTime.now().plusMinutes(-3);
            Long number = deviceApplicationRepository.countByApplicationEntityDetailId(applicationId);
            Long numberActiveNow = deviceApplicationRepository.countByApplicationEntityDetailIdAndHistoryApplicationEntitiesDetailCreatedDateBetween(applicationId, time, LocalDateTime.now());
            result.add(new PieChart(number - numberActiveNow, "Not Active"));
            result.add(new PieChart(numberActiveNow, "Active"));
        }
        return result;
    }

    @Override
    public List<AreaChartHisPerf> getAreaChartApplication(Long deviceId, String packagename, Integer dayago) {
        List<AreaChartHisPerf> result = new ArrayList<>();

        LocalDate DaysAgo = LocalDate.now().minusDays(dayago);
        LocalDateTime start = LocalDateTime.of(DaysAgo, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(DaysAgo, LocalTime.MAX);
        DeviceApplicationEntity deviceApplicationEntity = deviceApplicationRepository.findOneByDeviceAppEntityDetailIdAndApplicationEntityDetailPackagename(deviceId, packagename);
        if (deviceApplicationEntity == null){
            throw new ResourceNotFoundException("application" + packagename + " not install in device with id " + deviceId);
        }
        List<HistoryApplicationEntity> historyApplicationEntities = historyApplicationRepository.findAllByHistoryDeviceApplicationEntityIdAndCreatedDateBetween(deviceApplicationEntity.getId(), start, end);
        if (historyApplicationEntities == null) {
            throw new ResourceNotFoundException("application" + packagename + " not active in device with id " + deviceId + " " + dayago + " ago");
        }

        for (int i = 0; i < 480; i++) {

            HistoryApplicationEntity entity = new HistoryApplicationEntity();
            if (historyApplicationEntities.size() != 0) {
                entity = historyApplicationEntities.get(0);
            } else {
                AreaChartHisPerf areaChartHisPerf = new AreaChartHisPerf(start.plusMinutes(i * 3), 0.0, 0.0);
                result.add(areaChartHisPerf);
                continue;
            }

            while (entity.getCreatedDate().isBefore(start.plusMinutes(i * 3).plusMinutes(-1).plusSeconds(-30))) {
                historyApplicationEntities.remove(0);
                entity = historyApplicationEntities.get(0);
            }

            if (entity.getCreatedDate().plusMinutes(-1).plusSeconds(-30).isBefore(start.plusMinutes(i * 3))
                    && entity.getCreatedDate().plusMinutes(1).plusSeconds(30).isAfter(start.plusMinutes(i * 3))) {
                AreaChartHisPerf areaChartHisPerf = new AreaChartHisPerf(start.plusMinutes(i * 3), entity.getCpu(), entity.getMemory());
                result.add(areaChartHisPerf);
                historyApplicationEntities.remove(0);
            } else {
                AreaChartHisPerf areaChartHisPerf = new AreaChartHisPerf(start.plusMinutes(i * 3), 0.0, 0.0);
                result.add(areaChartHisPerf);
            }
        }

        return result;
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
        applicationRepository.findByPackagenameContainingOrderByCreatedDateDesc(packagename).forEach(applicationEntities::add);
        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findByPackagename(String packagename, Pageable pageable) {
        List<ApplicationDTO> result = new ArrayList<>();
        List<ApplicationEntity> applicationEntities = applicationRepository.findByPackagenameContainingOrderByCreatedDateDesc(packagename, pageable);
        for (ApplicationEntity item : applicationEntities) {
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }
}
