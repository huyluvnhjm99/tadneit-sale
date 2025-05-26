package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.dto.ItemDTO;
import com.tadneit.sale.common.entity.Category;
import com.tadneit.sale.common.filter.ItemFilter;
import com.tadneit.sale.exception.BusinessException;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.UUID;

public interface ItemService {
    long countItem();
    Page<ItemDTO> searchItems(ItemFilter request);
    ItemDTO getById(UUID id) throws BusinessException;
    ItemDTO create(ItemDTO dto) throws BusinessException;
    ItemDTO update(ItemDTO dto) throws BusinessException;
    void delete(UUID id);
}
