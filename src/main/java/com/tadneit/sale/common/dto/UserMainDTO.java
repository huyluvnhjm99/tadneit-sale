package com.tadneit.sale.common.dto;

import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.enumeration.SaleUserStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMainDTO extends BaseDTO {

    private UUID id;

    @NotNull(message = "{" + MessageResponse.USER_FULL_NAME_NOTNULL + "}")
    @Size(min = 3, max = 20, message = "{" + MessageResponse.USER_FULL_NAME_SIZE + "}")
    private String fullName;

    @NotNull(message = "{" + MessageResponse.USER_USERNAME_NOTNULL + "}")
    @Size(min = 3, max = 20, message = "{" + MessageResponse.USER_USERNAME_SIZE + "}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{" + MessageResponse.USER_USERNAME_PATTERN + "}")
    private String username;

    @NotNull(message = "{" + MessageResponse.PASSWORD_NOTNULL + "}")
    @Size(min = 6, max = 20, message = "{" + MessageResponse.PASSWORD_SIZE + "}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{" + MessageResponse.PASSWORD_PATTERN + "}")
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{6,20}$", message = "Password must include at least one letter, one number, and one special character")
    private String password;
    private String phone;
    private String email;
    private SaleUserRole role;
    private SaleUserStatus status;
}
