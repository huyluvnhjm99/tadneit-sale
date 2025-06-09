package com.tadneit.sale.service.Impl;

import com.tadneit.sale.common.dto.BannerDTO;
import com.tadneit.sale.common.dto.FileDTO;
import com.tadneit.sale.common.entity.Banner;
import com.tadneit.sale.common.enumeration.FileMappingType;
import com.tadneit.sale.common.mapper.BannerMapper;
import com.tadneit.sale.exception.BusinessException;
import com.tadneit.sale.repository.BannerRepository;
import com.tadneit.sale.service.BannerService;
import com.tadneit.sale.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final FileService fileService;
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    public long count() {
        return bannerRepository.count();
    }

    @Override
    public List<BannerDTO> getAll() {
        List<Banner> banners = bannerRepository.findAll();
        if (CollectionUtils.isEmpty(banners)) {
            return null;
        }

        final List<FileDTO> bannerIcons = fileService.getFileByMapping(FileMappingType.BANNER,
                banners.stream().map(Banner::getId).toList());
        final Map<UUID, List<FileDTO>> bannerIconsMap = bannerIcons.stream().collect(Collectors.groupingBy(FileDTO::getMappingId));

        return banners.stream()
                .map(c -> {
                    BannerDTO dto = bannerMapper.toDTO(c);
                    if (bannerIconsMap.containsKey(dto.getId())) {
                        List<FileDTO> categoryIcons = bannerIconsMap.get(dto.getId());
                        dto.setImgs(
                                categoryIcons
                                        .stream()
                                        .peek(img -> img.setUrl(fileService.getSignedUrl(img.getFilePath())))
                                        .collect(Collectors.toList())
                        );
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    public BannerDTO getById(UUID id) throws BusinessException {
        return bannerMapper.toDTO(bannerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Banner not found")));
    }

    @Override
    public BannerDTO createOrUpdate(BannerDTO dto) {
        Banner banner = bannerMapper.toEntity(dto);
        bannerRepository.save(banner);
        BannerDTO result = bannerMapper.toDTO(banner);

        if (Objects.nonNull(dto.getImgs())) {
            Set<FileDTO> images = dto.getImgs().stream().peek(img -> {
                img.setMappingId(banner.getId());
                img.setMappingType(FileMappingType.BANNER);
            }).collect(Collectors.toSet());

            result.setImgs(fileService.saveImages(images.stream().toList()));
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        bannerRepository.deleteById(id);
    }
}

