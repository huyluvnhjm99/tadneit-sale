package com.tadneit.sale.common.mapper;

import com.tadneit.sale.common.dto.UserDetailDTO;
import com.tadneit.sale.common.dto.UserMainDTO;
import com.tadneit.sale.common.entity.UserMain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMainMapper {

    @Mapping(target = "password", ignore = true)
    UserMainDTO toDTO(UserMain userMain);

    UserDetailDTO toDetailDTO(UserMain userMain);

    UserMain toEntity(UserMainDTO userMainDTO);
}
