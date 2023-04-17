package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.ApkConverter;
import com.vnptt.tms.dto.ApkDTO;
import com.vnptt.tms.entity.ApkEntity;
import com.vnptt.tms.repository.ApkRepository;
import com.vnptt.tms.service.IApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ApkService implements IApkService {

    @Autowired
    private ApkRepository apkRepository;

    @Autowired
    private ApkConverter apkConverter;

    @Override
    public ApkDTO save(ApkDTO apkDTO) {
        ApkEntity apkEntity = new ApkEntity();
        if (apkDTO.getId() != null){
            Optional <ApkEntity> oldApkEntity = apkRepository.findById(apkDTO.getId());
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
     *  find item with page number and totalPage number
     * @param pageable
     * @return
     */
    @Override
    public List<ApkDTO> findAll(Pageable pageable) {
        List<ApkEntity> entities = apkRepository.findAll(pageable).getContent();
        List<ApkDTO> result = new ArrayList<>();
        for(ApkEntity item : entities){
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    @Override
    public List<ApkDTO> findAll() {
        List<ApkEntity> entities = apkRepository.findAll();
        List<ApkDTO> result = new ArrayList<>();
        for(ApkEntity item : entities){
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    @Override
    public int totalItem(){
        return (int) apkRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            apkRepository.deleteById(item);
        }
    }
}
