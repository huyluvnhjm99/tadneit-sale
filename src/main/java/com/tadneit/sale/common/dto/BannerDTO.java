package com.tadneit.sale.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO extends BaseDTO {
    private UUID id;
    private String name;
    private String description;
    private List<FileDTO> imgs;
}
