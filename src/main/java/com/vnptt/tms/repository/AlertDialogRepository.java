package com.vnptt.tms.repository;

import com.vnptt.tms.entity.AlertDialogEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertDialogRepository extends JpaRepository<AlertDialogEntity, Long> {

    AlertDialogEntity findOneById(Long id);

    List<AlertDialogEntity> findAllByTitleContainingOrderByModifiedDateDesc(String message, Pageable pageable);


    List<AlertDialogEntity> findAllByOrderByModifiedDateDesc(Pageable pageable);

    Long countAllByTitleContaining(String title);
}
