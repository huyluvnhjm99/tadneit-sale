package com.tadneit.sale.common.mapper;

import com.tadneit.sale.common.dto.ItemDTO;
import com.tadneit.sale.common.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ItemMapper {

    ItemDTO toDTO(Item item);

    Item toEntity(ItemDTO dto);
}
