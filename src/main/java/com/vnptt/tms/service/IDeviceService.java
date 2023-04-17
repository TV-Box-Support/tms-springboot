package com.vnptt.tms.service;

import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.dto.DeviceDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDeviceService {
    DeviceDTO save(DeviceDTO deviceDTO);
    DeviceDTO findOne(Long id);
    int totalItem();
    void delete(Long[] ids);
    List<DeviceDTO> findAll(Pageable pageable);
    List<DeviceDTO> findAll();
}
