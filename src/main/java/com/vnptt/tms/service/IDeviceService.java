package com.vnptt.tms.service;

import com.vnptt.tms.api.output.chart.BarChart;
import com.vnptt.tms.api.output.chart.PieChart;
import com.vnptt.tms.api.output.studio.TerminalStudioOutput;
import com.vnptt.tms.dto.DeviceDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDeviceService {

    DeviceDTO save(DeviceDTO deviceDTO);

    DeviceDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<DeviceDTO> findAll(Pageable pageable);

    List<DeviceDTO> findAll();

    DeviceDTO findOneBySn(String ip, String serialnumber);

    List<DeviceDTO> findByLocation(String location, Pageable pageable);


    List<DeviceDTO> findAllDeviceRunNow(Pageable pageable);

    ResponseEntity<?> authenticateDevice(String serialnumber, String mac);

    List<DeviceDTO> findDeviceActive(int day, long hour, int minutes, Pageable pageable);

    List<DeviceDTO> findAllWithApplication(Long applicationId);

    List<DeviceDTO> findAllDeviceRunApp(Long applicationId);

    List<DeviceDTO> mapDeviceToListDevice(Long listDeviceId, Long[] deviceIds);

    void removeDeviceinListDevice(Long listDeviceId, Long deviceId);

    List<DeviceDTO> findDeviceInListDevice(Long listDeviceId);

    TerminalStudioOutput updateTerminalStudioInfo();

    List<PieChart> getTotalPieChart(String type);

    List<BarChart> getTotalAreaChart();

    DeviceDTO boxUpdate(String sn, DeviceDTO model);

    Long countDeviceRunNow();

    Long countDeviceActive(int day, long hour, int minutes);

    List<DeviceDTO> findByDescriptionAndSn(String search, Pageable pageable);

    Long countByDescriptionAndSn(String search);

//    List<DeviceDTO> findByDate(Date date, Pageable pageable);
//
//    List<DeviceDTO> findByDescriptionAndDate(Date dateOfManufacture, String description, Pageable pageable);
//
//    Long countByDescriptionAndDate(Date dateOfManufacture, String description);
//
//    Long countByDate(Date dateOfManufacture);

    List<DeviceDTO> findByDescriptionAndLocation(String location, String description, Pageable pageable);

    Long countByDescriptionAndLocation(String location, String description);

    Long countByLocation(String location);
}
