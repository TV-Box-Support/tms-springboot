package com.vnptt.tms.service;

import com.vnptt.tms.dto.ListDeviceDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IListDeviceService {
    ListDeviceDTO save(ListDeviceDTO listDeviceDTO);

    ListDeviceDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<ListDeviceDTO> findAll(Pageable pageable);

    List<ListDeviceDTO> findAll();

    ListDeviceDTO addListDeviceToUser(Long userId, Long listDeviceId);

    void removeListDeviceInUser(Long userId, Long listDeviceId);

    List<ListDeviceDTO> findListDeviceManagementByUser(Long userId);

    List<ListDeviceDTO> findAllWithName(String name, Pageable pageable);

    Long countWithName(String name);
}
