package com.tadneit.sale.common.entity;

import com.tadneit.sale.common.enumeration.SaleUserRole;
import com.tadneit.sale.common.enumeration.SaleUserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMain extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullName;
    private String username;
    private String password;
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private SaleUserRole role;

    @Enumerated(EnumType.STRING)
    private SaleUserStatus status = SaleUserStatus.INACTIVE;
}
