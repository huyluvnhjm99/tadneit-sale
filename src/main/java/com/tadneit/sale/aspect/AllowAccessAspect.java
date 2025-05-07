package com.tadneit.sale.aspect;

import com.tadneit.sale.common.annotation.AllowAccess;
import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class AllowAccessAspect {
    @Around("@annotation(allowAccess)")
    public Object checkAccess(ProceedingJoinPoint joinPoint, AllowAccess allowAccess) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BusinessException(MessageResponse.AUTHENTICATION_INVALID);
        }

        Set<String> userRoles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (SaleUserRole allowedRole : allowAccess.toUserRoles()) {
            String expectedRole = allowedRole.name();
            if (userRoles.contains(expectedRole)) {
                return joinPoint.proceed();
            }
        }

        throw new BusinessException(MessageResponse.AUTHENTICATION_INVALID);
    }
}
