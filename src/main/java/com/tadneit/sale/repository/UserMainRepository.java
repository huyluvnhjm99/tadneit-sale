package com.tadneit.sale.repository;

import com.tadneit.sale.common.entity.UserMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserMainRepository extends JpaRepository<UserMain, UUID>, JpaSpecificationExecutor<UserMain> {

    Optional<UserMain> findByUsername(String username);
}
