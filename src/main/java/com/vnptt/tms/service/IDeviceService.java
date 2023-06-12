package com.vnptt.tms.service;

import com.vnptt.tms.api.output.chart.AreaChart;
import com.vnptt.tms.api.output.chart.PieChart;
import com.vnptt.tms.api.output.studio.TerminalStudioOutput;
import com.vnptt.tms.dto.DeviceDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

public interface IDeviceService {

    DeviceDTO save(DeviceDTO deviceDTO);

    DeviceDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<DeviceDTO> findAll(Pageable pageable);

    List<DeviceDTO> findAll();

    DeviceDTO findOneBySn(String ip, String serialnumber);

    List<DeviceDTO> findByModelAndFirmwareVer(String model, String firmwareVer);

    List<DeviceDTO> findByModelAndFirmwareVer(String model, String firmwareVer, Pageable pageable);

    List<DeviceDTO> findByLocation(String location);

    List<DeviceDTO> findByDate(Date date);

    List<DeviceDTO> findAllDeviceRunNow();

    ResponseEntity<?> authenticateDevice(String serialnumber, String mac);

    List<DeviceDTO> findDeviceActive(int day, long hour, int minutes);

    List<DeviceDTO> findAllWithApplication(Long applicationId);

    List<DeviceDTO> findAllDeviceRunApp(Long applicationId);

    List<DeviceDTO> mapDeviceToListDevice(Long listDeviceId, Long[] deviceIds);

    void removeDeviceinListDevice(Long listDeviceId, Long deviceId);

    List<DeviceDTO> findDeviceInListDevice(Long listDeviceId);

    TerminalStudioOutput updateTerminalStudioInfo();

    List<PieChart> getTotalPieChart(String type);

    List<AreaChart> getTotalAreaChart();

    DeviceDTO boxUpdate(String sn, DeviceDTO model);
}
