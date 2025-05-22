package com.tadneit.sale.common.mapper;

import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.entity.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface FileMapper {

    FileDTO toDTO(File file);

    File toEntity(FileDTO dto);
}
