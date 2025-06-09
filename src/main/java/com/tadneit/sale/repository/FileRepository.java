package com.tadneit.sale.repository;

import com.tadneit.sale.common.entity.File;
import com.tadneit.sale.common.enumeration.FileMappingType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends BaseRepository<File, UUID> {
    List<File> findAllByMappingTypeAndMappingIdIn(FileMappingType mappingType, List<UUID> mappingIds);

    @Modifying
    void deleteAllByMappingTypeAndMappingIdIn(FileMappingType mappingType, List<UUID> mappingIds);
}

