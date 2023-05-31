package com.vnptt.tms.service;

import com.vnptt.tms.dto.ApplicationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IApplicationService {
    ApplicationDTO save(ApplicationDTO applicationDTO);

    ApplicationDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<ApplicationDTO> findAll(Pageable pageable);

    List<ApplicationDTO> findAll();

    List<ApplicationDTO> findByPackagename(String packagename);

    List<ApplicationDTO> findByPackagename(String packagename, Pageable pageable);

    List<ApplicationDTO> findAllOnDevice(Long deviceId);

    List<ApplicationDTO> findAllOnDevice(Long deviceId, String name);

    List<ApplicationDTO> findAllOnDevice(Long deviceId, Boolean isSystem);

    List<ApplicationDTO> findAllOnDevice(Long deviceId, String name, Boolean isSystem);

    ApplicationDTO addAppToDevice(Long deviceId, ApplicationDTO model);
}
