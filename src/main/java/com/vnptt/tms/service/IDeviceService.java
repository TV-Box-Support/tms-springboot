package com.vnptt.tms.service;

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

    List<DeviceDTO> findAllWithApplication(Long applicationId);

    List<DeviceDTO> findAllDeviceRunApp(Long applicationId);

    List<DeviceDTO> findAllDeviceRunNow();

    ResponseEntity<?> authenticateDevice(String serialnumber, String mac);
}
