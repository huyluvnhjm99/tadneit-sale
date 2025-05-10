package com.tadneit.sale.controller;

import com.tadneit.sale.common.annotation.AllowAccess;
import com.tadneit.sale.common.dto.ItemDTO;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.filter.ItemFilter;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/i")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public Page<ItemDTO> searchItems(ItemFilter filter) {
        return itemService.searchItems(filter);
    }

    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable UUID id) throws BusinessException {
        return itemService.getById(id);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PostMapping
    public ItemDTO create(@RequestBody ItemDTO dto) {
        return itemService.create(dto);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PutMapping
    public ItemDTO update(@RequestBody ItemDTO dto) throws BusinessException {
        return itemService.update(dto);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        itemService.delete(id);
    }
}

