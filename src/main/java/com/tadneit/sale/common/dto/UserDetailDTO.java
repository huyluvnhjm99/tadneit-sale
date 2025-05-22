package com.tadneit.sale.common.dto;

import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.enumeration.SaleUserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO extends BaseDTO {

    private UUID id;
    private String fullName;
    private String username;
    private String phone;
    private String email;
    private String avatarUrl;
    private LocalDateTime dob;
    private LocalDateTime lastLogin;
    private SaleUserRole role;
    private SaleUserStatus status;
    private FileDTO avatar;
}
