package com.tadneit.sale.common.filter;

import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.enumeration.SaleUserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMainFilter extends BaseFilter {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;
}
