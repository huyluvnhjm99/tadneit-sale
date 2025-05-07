package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.common.entity.UserMain;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.mapper.UserMainMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.UserMainRepository;
import com.tadneit.sale.security.JwtUtils;
import com.tadneit.sale.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    final private UserMainRepository userMainRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtUtils jwtUtils;
    final private UserMainMapper userMainMapper;

    public AuthServiceImpl(UserMainRepository userMainRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserMainMapper userMainMapper) {
        this.userMainRepository = userMainRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userMainMapper = userMainMapper;
    }

    @Override
    public String register(UserMainDTO user) throws BusinessException {
        if (userMainRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BusinessException(MessageResponse.REGISTRATION_USERNAME_ALREADY_EXISTED);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserMain userMain = userMainMapper.toEntity(user);
        userMain.setRole(SaleUserRole.CLIENT);
        userMainRepository.save(userMain);
        return MessageResponse.REGISTRATION_SUCCEED;
    }

    @Override
    public String login(String username, String rawPassword) throws BusinessException {
        UserMain user = userMainRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(MessageResponse.AUTHENTICATION_USER_NOT_FOUND));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BusinessException(MessageResponse.AUTHENTICATION_PASSWORD_INVALID);
        }

        return jwtUtils.generateToken(user);
    }
}
