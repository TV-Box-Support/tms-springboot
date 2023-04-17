package com.vnptt.tms.service;

import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDevicePolicyDetailnService {
    DevicePolicyDetailDTO save(DevicePolicyDetailDTO DevicePolicyDetailDTO);
    DevicePolicyDetailDTO findOne(Long id);
    int totalItem();
    void delete(Long[] ids);
    List<DevicePolicyDetailDTO> findAll(Pageable pageable);
    List<DevicePolicyDetailDTO> findAll();
}
