package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.dto.ItemDTO;
import com.tadneit.sale.common.entity.Category;
import com.tadneit.sale.common.entity.Item;
import com.tadneit.sale.common.enumeration.FileMappingType;
import com.tadneit.sale.common.filter.ItemFilter;
import com.tadneit.sale.common.mapper.ItemMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.CategoryRepository;
import com.tadneit.sale.repository.ItemRepository;
import com.tadneit.sale.service.FileService;
import com.tadneit.sale.service.ItemService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public long countItem() {
        return itemRepository.count();
    }

    public Page<ItemDTO> searchItems(ItemFilter itemFilter) {
        Specification<Item> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(itemFilter.getSearchKey())) {
                final String searchKey = itemFilter.getSearchKey().toLowerCase().trim();
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + searchKey + "%"
                );

                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + searchKey + "%"
                );

                Predicate brandPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("brand")),
                        "%" + searchKey + "%"
                );

                Predicate pricePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.toString(root.get("price"))),
                        "%" + searchKey + "%"
                );

                predicates.add(criteriaBuilder.or(
                        namePredicate,
                        brandPredicate,
                        descriptionPredicate,
                        pricePredicate)
                );
            }

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

            if (itemFilter.getCategoryId() != null) {
                Join<Item, Category> categories = root.join("categories", JoinType.LEFT);
                predicates.add(
                        criteriaBuilder.equal(categories.get("id"), itemFilter.getCategoryId())
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

        final List<FileDTO> listImages = fileService
                .getFileByMapping(FileMappingType.ITEM, items.get().map(Item::getId).toList());
        Page<ItemDTO> result = items.map(itemMapper::toDTO);
        if (!CollectionUtils.isEmpty(listImages)) {
            final Map<UUID, List<FileDTO>> mapImages = listImages.stream()
                    .collect(Collectors.groupingBy(FileDTO::getMappingId));
            for (ItemDTO item : result.getContent()) {
                if (mapImages.containsKey(item.getId())) {
                    item.setImages(new HashSet<>(mapImages.get(item.getId())));
                }
            }
        }
        return result;
    }

    public ItemDTO getById(UUID id) throws BusinessException {
        return itemMapper.toDTO(itemRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Item not found")));
    }

    public ItemDTO create(ItemDTO dto) throws BusinessException {
        if (itemRepository.existsItemByName(dto.getName())) {
            throw new BusinessException(MessageResponse.NAME_DUPLICATED);
        }

        Item item = itemMapper.toEntity(dto);
        item.setCategories(fetchCategories(dto.getCategories()));

        ItemDTO result = itemMapper.toDTO(itemRepository.save(item));
        if (Objects.nonNull(dto.getImages())) {
            Set<FileDTO> images = dto.getImages().stream().peek(img -> {
                img.setMappingId(item.getId());
                img.setMappingType(FileMappingType.ITEM);
            }).collect(Collectors.toSet());

            result.setImages(new HashSet<>(fileService.saveImages(images.stream().toList())));
        }

        return result;
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

