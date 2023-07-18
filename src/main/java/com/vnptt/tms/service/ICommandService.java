package com.vnptt.tms.service;

import com.vnptt.tms.dto.CommandDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommandService {
    CommandDTO save(CommandDTO commandDTO);

    CommandDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<CommandDTO> findAll(Pageable pageable);

    List<CommandDTO> findAll();

    List<CommandDTO> findAllWithName(String name, Pageable pageable);

    Long totalItemWithName(String name);
}
