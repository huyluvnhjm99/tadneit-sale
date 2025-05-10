package com.tadneit.sale.common.mapper;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO dto);
}
