package com.tadneit.sale.common.mapper;

import com.tadneit.sale.common.dto.BannerDTO;
import com.tadneit.sale.common.entity.Banner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerDTO toDTO(Banner banner);
    Banner toEntity(BannerDTO dto);
}
