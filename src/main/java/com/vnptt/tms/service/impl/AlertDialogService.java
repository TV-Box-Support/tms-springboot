package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.AlertDialogConverter;
import com.vnptt.tms.dto.AlertDialogDTO;
import com.vnptt.tms.entity.AlertDialogEntity;
import com.vnptt.tms.repository.AlertDialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AlertDialogService implements com.vnptt.tms.service.AlertDialogService {

    @Autowired
    private AlertDialogRepository AlertDialogRepository;

    @Autowired
    private AlertDialogConverter AlertDialogConverter;

    @Override
    public AlertDialogDTO save(AlertDialogDTO AlertDialogDTO) {
        AlertDialogEntity AlertDialogEntity = new AlertDialogEntity();
        if (AlertDialogDTO.getId() != null){
            Optional<AlertDialogEntity> oldCommandNotificationEntity = AlertDialogRepository.findById(AlertDialogDTO.getId());
            AlertDialogEntity = AlertDialogConverter.toEntity(AlertDialogDTO, oldCommandNotificationEntity.get());
        } else {
            AlertDialogEntity = AlertDialogConverter.toEntity(AlertDialogDTO);
        }
        AlertDialogEntity = AlertDialogRepository.save(AlertDialogEntity);
        return AlertDialogConverter.toDTO(AlertDialogEntity);
    }

    @Override
    public AlertDialogDTO findOne(Long id) {
        AlertDialogEntity entity = AlertDialogRepository.findOneById(id);
        return AlertDialogConverter.toDTO(entity);
    }

    /**
     *  find item with page number and totalPage number
     * @param pageable
     * @return
     */
    @Override
    public List<AlertDialogDTO> findAll(Pageable pageable) {
        List<AlertDialogEntity> entities = AlertDialogRepository.findAllByOrderByModifiedDateDesc(pageable);
        List<AlertDialogDTO> result = new ArrayList<>();
        for(AlertDialogEntity item : entities){
            AlertDialogDTO AlertDialogDTO = AlertDialogConverter.toDTO(item);
            result.add(AlertDialogDTO);
        }
        return result;
    }

    @Override
    public List<AlertDialogDTO> findAll() {
        List<AlertDialogEntity> entities = AlertDialogRepository.findAll();
        List<AlertDialogDTO> result = new ArrayList<>();
        for(AlertDialogEntity item : entities){
            AlertDialogDTO AlertDialogDTO = AlertDialogConverter.toDTO(item);
            result.add(AlertDialogDTO);
        }
        return result;
    }

    @Override
    public List<AlertDialogDTO> findAllWithMessage(String message, Pageable pageable) {
        List<AlertDialogEntity> entities = AlertDialogRepository.findAllByMessageContainingOrderByModifiedDateDesc(message, pageable);
        List<AlertDialogDTO> result = new ArrayList<>();
        for(AlertDialogEntity item : entities){
            AlertDialogDTO AlertDialogDTO = AlertDialogConverter.toDTO(item);
            result.add(AlertDialogDTO);
        }
        return result;
    }

    @Override
    public Long countAllWithMessage(String message) {
        return AlertDialogRepository.countAllByMessageContaining(message);
    }

    @Override
    public int totalItem(){
        return (int) AlertDialogRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            AlertDialogRepository.deleteById(item);
        }
    }
}
