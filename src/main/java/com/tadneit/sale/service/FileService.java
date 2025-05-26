package com.tadneit.sale.service;

import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.enumeration.FileMappingType;
import com.tadneit.sale.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    String getSignedUrl(String filePath);
    String getUrl(String filePath);
    List<FileDTO> getFileByMapping(FileMappingType type, List<UUID> mappingIds);

    void deleteFileByMapping(FileMappingType type, List<UUID> mappingIds);

    FileDTO saveImage(FileDTO dto);
    List<FileDTO> saveImages(List<FileDTO> dtos);
    FileDTO uploadFile(MultipartFile file);
    FileDTO uploadImage(MultipartFile file) throws BusinessException;
}
