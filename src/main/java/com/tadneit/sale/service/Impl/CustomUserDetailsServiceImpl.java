package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.entity.UserMain;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.repository.UserMainRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    final private UserMainRepository userMainRepository;

    public CustomUserDetailsServiceImpl(UserMainRepository userMainRepository) {
        this.userMainRepository = userMainRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMain user = userMainRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessageResponse.AUTHENTICATION_USER_NOT_FOUND));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user.getRole())
        );
    }

    private List<SimpleGrantedAuthority> getAuthorities(SaleUserRole role) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }
}
