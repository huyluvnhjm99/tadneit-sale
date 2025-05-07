package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.UserDetailDTO;
import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.common.filter.UserMainFilter;
import com.tadneit.sale.exception.BusinessException;
import org.springframework.data.domain.Page;


public interface UserMainService {
    UserDetailDTO getUserProfile() throws BusinessException;

    Page<UserMainDTO> retrieveAllUserMainDTOs(UserMainFilter filter);
}
