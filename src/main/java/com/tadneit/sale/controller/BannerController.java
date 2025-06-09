package com.tadneit.sale.controller;

import com.tadneit.sale.common.annotation.AllowAccess;
import com.tadneit.sale.common.dto.BannerDTO;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.service.BannerService;
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
@RequestMapping("/b")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/count")
    public long count() {
        return bannerService.count();
    }

    @GetMapping
    public List<BannerDTO> getAll() {
        return bannerService.getAll();
    }

    @GetMapping("/{id}")
    public BannerDTO getById(@PathVariable UUID id) throws BusinessException {
        return bannerService.getById(id);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PostMapping
    public BannerDTO create(@RequestBody BannerDTO dto) {
        return bannerService.createOrUpdate(dto);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PutMapping
    public BannerDTO update(@RequestBody BannerDTO dto) throws BusinessException {
        return bannerService.createOrUpdate(dto);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        bannerService.delete(id);
    }
}

