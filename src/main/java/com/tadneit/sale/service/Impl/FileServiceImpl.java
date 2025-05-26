package com.tadneit.sale.service.Impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.tadneit.sale.common.constant.MessageResponse;
import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.entity.File;
import com.tadneit.sale.common.enumeration.FileCategory;
import com.tadneit.sale.common.enumeration.FileMappingType;
import com.tadneit.sale.common.mapper.FileMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.FileRepository;
import com.tadneit.sale.security.JwtUtils;
import com.tadneit.sale.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final StorageClient storageClient;
    private final Firestore firestore;
    private final Storage storage;
    private final String bucketName;

    private static final Cache<String, String> IMAGE_SIGN_URL_CACHE = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp",
            "image/heic",
            "image/heif",
            "image/heics",
            "image/bmp",
            "image/tiff",
            "application/octet-stream"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String IMG_PATH = "img/";

    @Override
    public String getSignedUrl(String filePath) {
        return IMAGE_SIGN_URL_CACHE.get(filePath, value -> {
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filePath).build();

            URL signedUrl = storage.signUrl(
                    blobInfo,
                    24, TimeUnit.HOURS,
                    Storage.SignUrlOption.withV4Signature()
            );

            return signedUrl.toString();
        });
    }

    @Override
    public String getUrl(String filePath) {
        return IMAGE_SIGN_URL_CACHE.get(filePath, value -> {
            final String encodedPath = java.net.URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            return String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    bucketName,
                    encodedPath
            );
        });
    }

    @Override
    public List<FileDTO> getFileByMapping(FileMappingType type, List<UUID> mappingIds) {
        List<File> files = fileRepository.findAllByMappingTypeAndMappingIdIn(type, mappingIds);
        if (CollectionUtils.isEmpty(files)) {
            return null;
        }

        return files.stream().map((file -> {
            FileDTO fileDTO = fileMapper.toDTO(file);
            fileDTO.setUrl(getSignedUrl(fileDTO.getFilePath()));
            return fileDTO;
        })).toList();
    }

    @Override
    public void deleteFileByMapping(FileMappingType type, List<UUID> mappingIds) {
        fileRepository.deleteAllByMappingTypeAndMappingIdIn(type, mappingIds);
    }

    @Override
    public FileDTO saveImage(FileDTO dto) {
        File file = fileMapper.toEntity(dto);
        fileRepository.save(file);
        return fileMapper.toDTO(file);
    }

    @Override
    public List<FileDTO> saveImages(List<FileDTO> fileDTOS) {
        List<File> files = fileRepository.saveAll(fileDTOS.stream().map(fileMapper::toEntity).toList());
        return files.stream().map(fileMapper::toDTO).toList();
    }

    @Override
    public FileDTO uploadFile(MultipartFile file) {
        return null;
    }

    @Override
    public FileDTO uploadImage(MultipartFile file) throws BusinessException {
        validateImgFile(file);
        try {
            return handleFileUploading(file, FileCategory.IMAGE);
        } catch (Exception e) {
            throw new BusinessException(MessageResponse.UPLOAD_FAILED);
        }
    }

    private FileDTO handleFileUploading(MultipartFile file, FileCategory fileCategory) throws IOException {
        String path = StringUtils.EMPTY;
        if (FileCategory.IMAGE.equals(fileCategory)) {
            path = IMG_PATH;
        }

        final String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        final String filePath = path + fileName;
        storageClient.bucket().create(filePath, file.getBytes(), file.getContentType());
        final String downloadUrl = "https://storage.googleapis.com/" + bucketName + "/" + filePath;

        Map<String, Object> fileMetadata = new HashMap<>();
        fileMetadata.put("fileName", fileName);
        fileMetadata.put("originalName", file.getOriginalFilename());
        fileMetadata.put("contentType", file.getContentType());
        fileMetadata.put("size", file.getSize());
        fileMetadata.put("uploadedBy", JwtUtils.getCurrentUsername());
        fileMetadata.put("uploadDate", new Date());
        fileMetadata.put("downloadUrl", downloadUrl);

        DocumentReference docRef = firestore.collection("files").document();
        docRef.set(fileMetadata);

        File fileEntity = new File();
        fileEntity.setFileId(docRef.getId());
        fileEntity.setFileName(fileName);
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setFilePath(filePath);
        fileEntity.setCreatedBy(JwtUtils.getCurrentUsername());
        fileEntity.setCategory(fileCategory);
        fileRepository.save(fileEntity);

        return fileMapper.toDTO(fileEntity);
    }

    private void validateImgFile(MultipartFile file) throws BusinessException {
        if (file.isEmpty()) {
            throw new BusinessException(MessageResponse.FILE_IS_EMPTY);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(MessageResponse.FILES_SIZE_TOO_BIG);
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(MessageResponse.FILE_TYPE_IS_INVALID);
        }
    }
}

