package com.vnptt.tms.service;

import com.vnptt.tms.dto.ApkDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IApkService {
    ApkDTO save(ApkDTO apkDTO);
    ApkDTO findOne(Long id);
    int totalItem();
    void delete(Long[] ids);
    List<ApkDTO> findAll(Pageable pageable);
    List<ApkDTO> findAll();
}
