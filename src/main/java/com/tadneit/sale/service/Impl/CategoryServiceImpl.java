package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.entity.Category;
import com.tadneit.sale.common.mapper.CategoryMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.CategoryRepository;
import com.tadneit.sale.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public CategoryDTO getById(UUID id) throws BusinessException {
        return categoryMapper.toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found")));
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO update(CategoryDTO dto) throws BusinessException {
        Category category = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException("Category not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}

