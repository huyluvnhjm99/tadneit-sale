package com.tadneit.sale.controller;

import com.tadneit.sale.common.annotation.AllowAccess;
import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/f")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PostMapping("/u")
    public FileDTO uploadFile(@RequestPart("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PostMapping("/u-i")
    public FileDTO uploadImage(@RequestPart("file") MultipartFile file) throws BusinessException {
        return fileService.uploadImage(file);
    }

    //@AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @GetMapping("/l")
    public String getFileUrl(String path) {
        return fileService.getUrl(path);
    }

//    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable UUID id) {
//        itemService.delete(id);
//    }
}

