package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.dto.ItemDTO;
import com.tadneit.sale.common.entity.Category;
import com.tadneit.sale.common.entity.Item;
import com.tadneit.sale.common.filter.ItemFilter;
import com.tadneit.sale.common.mapper.ItemMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.CategoryRepository;
import com.tadneit.sale.repository.ItemRepository;
import com.tadneit.sale.service.ItemService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CategoryRepository categoryRepository;

    public Page<ItemDTO> searchItems(ItemFilter itemFilter) {
        Specification<Item> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (itemFilter.getName() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + itemFilter.getName().toLowerCase() + "%"
                        )
                );
            }

            if (itemFilter.getDescription() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                "%" + itemFilter.getDescription().toLowerCase() + "%"
                        )
                );
            }

            if (itemFilter.getCategoryName() != null) {
                Join<Item, Category> categories = root.join("categories", JoinType.LEFT);
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(categories.get("name")),
                                "%" + itemFilter.getCategoryName().toLowerCase() + "%"
                        )
                );
            }

            if (itemFilter.getCreatedDateFrom() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createdDate"),
                                itemFilter.getCreatedDateTo()
                        )
                );
            }

            if (itemFilter.getCreatedDateTo() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("createdDate"),
                                itemFilter.getCreatedDateTo()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        final Pageable pageable = PageRequest.of(
                itemFilter.getPageOrDefault(), itemFilter.getSizeOrDefault(), itemFilter.getSortOrDefault());
        Page<Item> items = itemRepository.findAll(spec, pageable);
        return items.map(itemMapper::toDTO);
    }

    public ItemDTO getById(UUID id) throws BusinessException {
        return itemMapper.toDTO(itemRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Item not found")));
    }

    public ItemDTO create(ItemDTO dto) {
        Item item = itemMapper.toEntity(dto);
        item.setCategories(fetchCategories(dto.getCategories()));
        return itemMapper.toDTO(itemRepository.save(item));
    }

    public ItemDTO update(ItemDTO dto) throws BusinessException {
        Item item = itemRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException("Item not found"));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setCategories(fetchCategories(dto.getCategories()));
        return itemMapper.toDTO(itemRepository.save(item));
    }

    public void delete(UUID id) {
        itemRepository.deleteById(id);
    }

    public Set<Category> fetchCategories(Set<CategoryDTO> categoryDTOs) {
        if (categoryDTOs == null || categoryDTOs.isEmpty()) return Collections.emptySet();
        Set<UUID> ids = categoryDTOs.stream().map(CategoryDTO::getId).collect(Collectors.toSet());
        return new HashSet<>(categoryRepository.findAllById(ids));
    }
}

