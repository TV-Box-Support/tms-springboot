package com.vnptt.tms.repository;

import com.vnptt.tms.entity.ListDeviceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ListDeviceRepository extends JpaRepository<ListDeviceEntity, Long> {

    ListDeviceEntity findOneById(Long id);

    ListDeviceEntity findOneByName(String all);

    List<ListDeviceEntity> findAllByNameContainingOrderByModifiedDateDesc(String name, Pageable pageable);

    Long countAllByNameContaining(String name);

}
