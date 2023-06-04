package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.ListDeviceConverter;
import com.vnptt.tms.dto.ListDeviceDTO;
import com.vnptt.tms.entity.ListDeviceEntity;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.ListDeviceRepository;
import com.vnptt.tms.repository.UserRepository;
import com.vnptt.tms.service.IListDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ListDeviceService implements IListDeviceService {

    @Autowired
    private ListDeviceRepository listDeviceRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListDeviceConverter listDeviceConverter;


    /**
     * unnecessary (only use to test)
     * save to database when post and put app
     *
     * @return
     */
    @Override
    public ListDeviceDTO save(ListDeviceDTO listDeviceDTO) {
        ListDeviceEntity listDeviceEntity = new ListDeviceEntity();
        if (listDeviceDTO.getId() != null) {
            Optional<ListDeviceEntity> oldListDeviceEntity = listDeviceRepository.findById(listDeviceDTO.getId());
            listDeviceEntity = listDeviceConverter.toEntity(listDeviceDTO, oldListDeviceEntity.get());
        } else {
            listDeviceEntity = listDeviceConverter.toEntity(listDeviceDTO);
        }
        listDeviceEntity = listDeviceRepository.save(listDeviceEntity);
        return listDeviceConverter.toDTO(listDeviceEntity);
    }

    /**
     * find app with id
     *
     * @param id
     * @return
     */
    @Override
    public ListDeviceDTO findOne(Long id) {
        ListDeviceEntity entity = listDeviceRepository.findOneById(id);
        return listDeviceConverter.toDTO(entity);
    }

    /**
     * total item app on database
     *
     * @return
     */
    @Override
    public int totalItem() {
        return (int) listDeviceRepository.count();
    }

    /**
     * Delete app on database
     *
     * @param ids list id app
     */
    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            listDeviceRepository.deleteById(item);
        }
    }

    /**
     * find all app with pageable
     *
     * @param pageable
     * @return
     */
    @Override
    public List<ListDeviceDTO> findAll(Pageable pageable) {
        List<ListDeviceEntity> entities = listDeviceRepository.findAll(pageable).getContent();
        List<ListDeviceDTO> result = new ArrayList<>();
        for (ListDeviceEntity item : entities) {
            ListDeviceDTO ListDeviceDTO = listDeviceConverter.toDTO(item);
            result.add(ListDeviceDTO);
        }
        return result;
    }

    /**
     * find all app nomal
     *
     * @return
     */
    @Override
    public List<ListDeviceDTO> findAll() {
        List<ListDeviceEntity> entities = listDeviceRepository.findAll();
        List<ListDeviceDTO> result = new ArrayList<>();
        for (ListDeviceEntity item : entities) {
            ListDeviceDTO ListDeviceDTO = listDeviceConverter.toDTO(item);
            result.add(ListDeviceDTO);
        }
        return result;
    }

}
