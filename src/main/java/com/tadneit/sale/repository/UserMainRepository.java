package com.tadneit.sale.repository;

import com.tadneit.sale.common.entity.UserMain;

import java.util.Optional;
import java.util.UUID;

public interface UserMainRepository extends BaseRepository<UserMain, UUID> {

    Optional<UserMain> findByUsername(String username);
}
