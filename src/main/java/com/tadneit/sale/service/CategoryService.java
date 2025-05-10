package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.exception.BusinessException;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDTO> getAll();
    CategoryDTO getById(UUID id) throws BusinessException;
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO update(CategoryDTO dto) throws BusinessException;
    void delete(UUID id);
}
