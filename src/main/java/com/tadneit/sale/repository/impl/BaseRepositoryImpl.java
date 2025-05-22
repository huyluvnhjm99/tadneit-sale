package com.tadneit.sale.repository.impl;

import com.tadneit.sale.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.Optional;

public class BaseRepositoryImpl<T, UUID extends Serializable>
        extends SimpleJpaRepository<T, UUID> implements BaseRepository<T, UUID> {

    private final EntityManager entityManager;
    private final Class<T> domainClass;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    @Override
    public Optional<T> findByIdAndActiveAndNotDeleted(UUID id) {
        String query = "SELECT e FROM " + domainClass.getSimpleName() + " e WHERE e.id = :id AND e.deleted = false";
        return entityManager.createQuery(query, domainClass)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }
}

