package com.vnptt.tms.service.impl;


import com.vnptt.tms.converter.ApplicationConverter;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.repository.ApplicationRepository;
import com.vnptt.tms.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService implements IApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;


    @Autowired
    private ApplicationConverter applicationConverter;

    @Override
    public ApplicationDTO save(ApplicationDTO applicationDTO) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        if (applicationDTO.getId() != null){
            Optional<ApplicationEntity> oldApplicationEntity = applicationRepository.findById(applicationDTO.getId());
            applicationEntity = applicationConverter.toEntity(applicationDTO, oldApplicationEntity.orElse(applicationEntity));
        } else {
            applicationEntity = applicationConverter.toEntity(applicationDTO);
        }
        applicationEntity = applicationRepository.save(applicationEntity);
        return applicationConverter.toDTO(applicationEntity);
    }

    @Override
    public ApplicationDTO findOne(Long id) {
        ApplicationEntity entity = applicationRepository.findOneById(id);
        return applicationConverter.toDTO(entity);
    }

    @Override
    public int totalItem() {
        return (int) applicationRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            applicationRepository.deleteById(item);
        }
    }

    @Override
    public List<ApplicationDTO> findAll(Pageable pageable) {
        List<ApplicationEntity> entities = applicationRepository.findAll(pageable).getContent();
        List<ApplicationDTO> result = new ArrayList<>();
        for(ApplicationEntity item : entities){
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

    @Override
    public List<ApplicationDTO> findAll() {
        List<ApplicationEntity> entities = applicationRepository.findAll();
        List<ApplicationDTO> result = new ArrayList<>();
        for(ApplicationEntity item : entities){
            ApplicationDTO applicationDTO = applicationConverter.toDTO(item);
            result.add(applicationDTO);
        }
        return result;
    }

}
