package com.tadneit.sale.controller;

import com.tadneit.sale.common.annotation.AllowAccess;
import com.tadneit.sale.common.dto.CategoryDTO;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/c")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable UUID id) throws BusinessException {
        return categoryService.getById(id);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        return categoryService.create(dto);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PutMapping
    public CategoryDTO update(@RequestBody CategoryDTO dto) throws BusinessException {
        return categoryService.update(dto);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}

