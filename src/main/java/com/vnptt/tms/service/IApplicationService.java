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
}
