package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.BannerDTO;
import com.tadneit.sale.exception.BusinessException;

import java.util.List;
import java.util.UUID;

public interface BannerService {
    long count();
    List<BannerDTO> getAll();
    BannerDTO getById(UUID id) throws BusinessException;
    BannerDTO createOrUpdate(BannerDTO dto);
    void delete(UUID id);
}
