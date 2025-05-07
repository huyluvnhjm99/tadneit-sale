package com.tadneit.sale.common.annotation;

import com.tadneit.sale.common.enumeration.SaleUserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowAccess {
    SaleUserRole[] toUserRoles() default {};
}
