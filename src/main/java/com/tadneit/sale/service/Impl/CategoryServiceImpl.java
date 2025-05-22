package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.entity.Category;
import com.tadneit.sale.common.enumeration.FileMappingType;
import com.tadneit.sale.common.mapper.CategoryMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.CategoryRepository;
import com.tadneit.sale.service.CategoryService;
import com.tadneit.sale.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public long countCategory() {
        return categoryRepository.count();
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        if (CollectionUtils.isEmpty(categories)) {
            return null;
        }

        final List<FileDTO> categoryIcons = fileService.getFileByMapping(FileMappingType.CATEGORY,
                        categories.stream().map(Category::getId).toList());
        final Map<UUID, FileDTO> categoryIconsMap = categoryIcons.stream().collect(
                Collectors.toMap(FileDTO::getMappingId, Function.identity(), (f1, f2) -> f1));

        return categories.stream()
                .map(c -> {
                    CategoryDTO dto = categoryMapper.toDTO(c);
                    if (categoryIconsMap.containsKey(dto.getId())) {
                        FileDTO categoryIcon = categoryIconsMap.get(dto.getId());
                        categoryIcon.setUrl(fileService.getUrl(categoryIcon.getFilePath()));
                        dto.setImg(categoryIcon);
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    public CategoryDTO getById(UUID id) throws BusinessException {
        return categoryMapper.toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found")));
    }

    @Override
    public CategoryDTO createOrUpdate(CategoryDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        categoryRepository.save(category);
        CategoryDTO result = categoryMapper.toDTO(category);

        if (Objects.nonNull(dto.getImg())) {
            dto.getImg().setMappingId(category.getId());
            dto.getImg().setMappingType(FileMappingType.CATEGORY);
            FileDTO fileDto = fileService.saveImage(dto.getImg());
            result.setImg(fileDto);
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}

