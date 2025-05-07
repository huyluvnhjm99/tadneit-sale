package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.exception.BusinessException;

public interface AuthService {
    String register(UserMainDTO user) throws BusinessException;

    String login(String username, String password) throws BusinessException;
}
