package com.tadneit.sale.controller;

import com.tadneit.sale.common.annotation.AllowAccess;
import com.tadneit.sale.common.dto.UserDetailDTO;
import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.filter.UserMainFilter;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.service.UserMainService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/u")
public class UserMainController {

    final private UserMainService userMainService;

    public UserMainController(UserMainService userMainService) {
        this.userMainService = userMainService;
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER, SaleUserRole.CLIENT})
    @PostMapping("/profile")
    public ResponseEntity<UserDetailDTO> getUserProfile() throws BusinessException {
        return ResponseEntity.ok(userMainService.getUserProfile());
    }

    @AllowAccess(toUserRoles = {SaleUserRole.ADMINISTRATOR, SaleUserRole.MANAGER})
    @PostMapping("/all")
    public ResponseEntity<Page<UserMainDTO>> getAllUsers(@RequestBody UserMainFilter filter) {
        Page<UserMainDTO> userPage = userMainService.retrieveAllUserMainDTOs(filter);
        return ResponseEntity.ok(userPage);
    }
}
