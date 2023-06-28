package com.vnptt.tms.service.impl;

import com.vnptt.tms.config.FileStorageProperties;
import com.vnptt.tms.converter.ApkConverter;
import com.vnptt.tms.dto.ApkDTO;
import com.vnptt.tms.entity.ApkEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.exception.FileNotFoundException;
import com.vnptt.tms.exception.FileStorageException;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApkRepository;
import com.vnptt.tms.repository.PolicyRepository;
import com.vnptt.tms.service.IApkService;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApkService implements IApkService {

    private final Path fileStorageLocation;

    @Autowired
    public ApkService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored. " + ex);
        }
    }

    @Autowired
    private ApkRepository apkRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ApkConverter apkConverter;

    @Override
    public ApkDTO save(ApkDTO apkDTO) {
        ApkEntity apkEntity = new ApkEntity();
        if (apkDTO.getId() != null) {
            Optional<ApkEntity> oldApkEntity = apkRepository.findById(apkDTO.getId());
            apkEntity = apkConverter.toEntity(apkDTO, oldApkEntity.get());
        } else {
            apkEntity = apkConverter.toEntity(apkDTO);
        }
        apkEntity = apkRepository.save(apkEntity);
        return apkConverter.toDTO(apkEntity);
    }

    @Override
    public ApkDTO findOne(Long id) {
        ApkEntity entity = apkRepository.findOneById(id);
        return apkConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<ApkDTO> findAll(Pageable pageable) {
        List<ApkEntity> entities = apkRepository.findAll(pageable).getContent();
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity item : entities) {
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    @Override
    public List<ApkDTO> findAll() {
        List<ApkEntity> entities = apkRepository.findAll();
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity item : entities) {
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    /**
     * find all apk in a policy
     *
     * @param policyId has required
     * @return
     */
    @Override
    public List<ApkDTO> findAllOnPolicy(Long policyId) {
        if (!apkRepository.existsById(policyId)) {
            throw new ResourceNotFoundException("Not found policy with id = " + policyId);
        }
        List<ApkEntity> apkEntities = apkRepository.findApkEntitiesByPolicyEntitiesIdOrderByModifiedDateDesc(policyId);
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity entity : apkEntities) {
            ApkDTO apkDTO = apkConverter.toDTO(entity);
            result.add(apkDTO);
        }

        return result;
    }

    /**
     * add apk to policy
     *
     * @param policyId
     * @param apkId
     * @return
     */
    @Override
    public ApkDTO addApkToPolicy(Long policyId, Long apkId) {
        ApkEntity apkEntity = policyRepository.findById(policyId).map(policy -> {
            ApkEntity apk = apkRepository.findById(apkId)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found apk with id = " + apkId));

            // check if apk has still
            List<ApkEntity> apkEntities = policy.getApkEntitiesPolicy();
            for (ApkEntity item : apkEntities) {
                if (item.equals(apk)) {
                    return apk;
                }
            }
            //map and add apk to policy
            policy.addApk(apk);
            policyRepository.save(policy);
            return apk;
        }).orElseThrow(() -> new ResourceNotFoundException("Not found policy with id = " + policyId));
        return apkConverter.toDTO(apkEntity);
    }

    /**
     * remove apk in pilicy
     *
     * @param policyId
     * @param apkId
     */
    @Override
    public void removeApkinPolicy(Long policyId, Long apkId) {
        PolicyEntity policyEntity = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found policy with id = " + policyId));

        List<ApkEntity> apkEntities = policyEntity.getApkEntitiesPolicy();
        boolean remove = false;
        for (ApkEntity entity : apkEntities) {
            if (Objects.equals(entity.getId(), apkId)) {
                remove = true;
            }
        }
        if (remove) {
            policyEntity.removeApk(apkId);
            policyRepository.save(policyEntity);
        } else {
            throw new ResourceNotFoundException("policy don't have apk with id = " + apkId);
        }

    }

    /**
     * add file apk to server and save data to database
     *
     * @param file
     * @return
     */
    @Override
    public ApkDTO saveFile(MultipartFile file) {
        String packagename = storeFile(file);

        //create link to dowload apk
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/TMS/api/downloadFile/")
                .path(packagename)
                .toUriString();

        //String filePath = "/media/thanhchung/data/data/" + packagename;
        String filePath = fileStorageLocation + "/" + packagename;
        String md5 = checkSumApacheCommons(filePath);
        ApkEntity apkEntity = new ApkEntity();
        apkEntity.setPackagename(packagename);
        apkEntity.setApkfileUrl(fileDownloadUri);
        apkEntity.setPackagesize(file.getSize());
        apkEntity.setMd5(md5);

        // use to get apk version
        try {
            ApkFile apkFile = new ApkFile(filePath);
            ApkMeta apkMeta = apkFile.getApkMeta();
            apkEntity.setVersion(String.valueOf(apkMeta.getVersionCode()));
        } catch (IOException e) {
            throw new RuntimeException("the file you use is not apk");
        }

        apkEntity = apkRepository.save(apkEntity);
        return apkConverter.toDTO(apkEntity);
    }

    @Override
    public int totalItem() {
        return (int) apkRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            apkRepository.deleteById(item);
        }
    }

    /**
     * Get file name from multipartFile
     *
     * @param file
     * @return
     */
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again! Cause: ex");
        }
    }

    /**
     * used to let the box download the apk as Resource
     *
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName + " Cause: " + ex);
        }
    }

    @Override
    public List<ApkDTO> findAllWithPackageAndVersion(String packagename, String version, Pageable pageable) {
        List<ApkEntity> entities = apkRepository.findAllByPackagenameContainingAndVersionContainingOrderByModifiedDateDesc(packagename, version, pageable);
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity item : entities) {
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    @Override
    public Long countAllWithPackageAndVersion(String packagename, String version) {
        return apkRepository.countByPackagenameContainingAndVersionContainingOrderByModifiedDateDesc(packagename, version);
    }

    /**
     * use to ren checksum md5
     *
     * @param file
     * @return
     */
    public String checkSumApacheCommons(String file) {
        String checksum = null;
        try {
            checksum = DigestUtils.md5Hex(Files.newInputStream(Paths.get(file)));
        } catch (IOException ex) {
        }
        return checksum;
    }
}
