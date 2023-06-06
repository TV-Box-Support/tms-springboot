package com.vnptt.tms.service;

import com.vnptt.tms.dto.ApkDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IApkService {
    ApkDTO save(ApkDTO apkDTO);

    ApkDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<ApkDTO> findAll(Pageable pageable);

    List<ApkDTO> findAll();

    List<ApkDTO> findAllOnPolicy(Long policyId);

    ApkDTO addApkToPolicy(Long policyId, Long apkId);

    void removeApkinPolicy(Long policyId, Long apkId);

    ApkDTO saveFile(MultipartFile file);

    Resource loadFileAsResource(String fileName);

    List<ApkDTO> findAllWithPackageAndVersion(String packagename, String version, Pageable pageable);

    Long countAllWithPackageAndVersion(String packagename, String version);
}
