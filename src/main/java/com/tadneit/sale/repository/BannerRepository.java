package com.tadneit.sale.repository;

import com.tadneit.sale.common.entity.Banner;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BannerRepository extends BaseRepository<Banner, UUID> {
}

