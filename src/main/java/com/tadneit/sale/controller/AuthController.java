package com.tadneit.sale.controller;

import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserMainDTO userMainDTO) throws BusinessException {
        return ResponseEntity.ok(authService.register(userMainDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserMainDTO request) throws BusinessException {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
