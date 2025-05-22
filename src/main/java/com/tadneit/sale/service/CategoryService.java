package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.exception.BusinessException;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    long countCategory();
    List<CategoryDTO> getAll();
    CategoryDTO getById(UUID id) throws BusinessException;
    CategoryDTO createOrUpdate(CategoryDTO dto);
    void delete(UUID id);
}
