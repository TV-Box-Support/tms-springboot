package com.vnptt.tms.service;

import com.vnptt.tms.dto.AlertDialogDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlertDialogService {
    AlertDialogDTO save(AlertDialogDTO AlertDialogDTO);

    AlertDialogDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<AlertDialogDTO> findAll(Pageable pageable);

    List<AlertDialogDTO> findAll();

    List<AlertDialogDTO> findAllWithMessage(String message, Pageable pageable);

    Long countAllWithTitle(String message);
}
