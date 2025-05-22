package com.tadneit.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, UUID> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
    Optional<T> findByIdAndActiveAndNotDeleted(UUID id);
}

