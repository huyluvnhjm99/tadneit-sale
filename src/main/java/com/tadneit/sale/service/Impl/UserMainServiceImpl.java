package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.dto.UserDetailDTO;
import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.common.entity.UserMain;
import com.tadneit.sale.common.enumeration.FileMappingType;
import com.tadneit.sale.common.filter.UserMainFilter;
import com.tadneit.sale.common.mapper.UserMainMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.UserMainRepository;
import com.tadneit.sale.service.FileService;
import com.tadneit.sale.service.UserMainService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMainServiceImpl implements UserMainService {
    final private UserMainRepository userMainRepository;
    final private UserMainMapper userMainMapper;
    final private FileService fileService;

    @Override
    public UserDetailDTO getUserProfile() throws BusinessException {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserMain userMain = userMainRepository.findByUsername(username).orElseThrow(() -> new BusinessException(MessageResponse.AUTHENTICATION_USER_NOT_FOUND));
        final List<FileDTO> categoryIcons = fileService.getFileByMapping(FileMappingType.PROFILE, Collections.singletonList(userMain.getId()));
        UserDetailDTO result = userMainMapper.toDetailDTO(userMain);
        if (!CollectionUtils.isEmpty(categoryIcons)) {
            final String fileUrl = fileService.getSignedUrl(categoryIcons.getLast().getFilePath());
            result.setAvatarUrl(fileUrl);
        }

        return result;
    }

    @Override
    public UserDetailDTO saveUserProfile(UserDetailDTO dto) throws BusinessException{
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserMain userMain = userMainRepository.findByUsername(username).orElseThrow(() -> new BusinessException(MessageResponse.AUTHENTICATION_USER_NOT_FOUND));
        userMain.setFullName(dto.getFullName());
        userMain.setEmail(dto.getEmail());
        userMain.setPhone(dto.getPhone());
        userMain.setDob(dto.getDob());
        userMainRepository.save(userMain);
        if (Objects.nonNull(dto.getAvatar())) {
            dto.getAvatar().setMappingId(userMain.getId());
            dto.getAvatar().setMappingType(FileMappingType.PROFILE);
            fileService.saveImage(dto.getAvatar());
        }

        return this.getUserProfile();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserMainDTO> retrieveAllUserMainDTOs(UserMainFilter filter) {

        final Pageable pageable = PageRequest.of(
                filter.getPageOrDefault(), filter.getSizeOrDefault(), filter.getSortOrDefault());

        Specification<UserMain> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getUsername() != null && !filter.getUsername().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + filter.getUsername().toLowerCase().trim() + "%"));
            }
            if (filter.getFullName() != null && !filter.getFullName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + filter.getFullName().toLowerCase().trim() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + filter.getEmail().toLowerCase().trim() + "%"));
            }
            if (filter.getPhone() != null && !filter.getPhone().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + filter.getPhone().trim() + "%"));
            }
            if (filter.getRole() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), filter.getRole()));
            }
            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getCreatedDateFrom() != null && filter.getCreatedDateTo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), filter.getCreatedDateFrom()));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), filter.getCreatedDateTo()));
            }
            if (filter.getUpdatedDateFrom() != null && filter.getUpdatedDateTo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedDate"), filter.getUpdatedDateFrom()));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedDate"), filter.getUpdatedDateTo()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<UserMain> userMainPage = userMainRepository.findAll(spec, pageable);

        List<UserMainDTO> userMainDTOs = userMainPage.getContent().stream()
                .map(userMainMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(userMainDTOs, pageable, userMainPage.getTotalElements());
    }
}
