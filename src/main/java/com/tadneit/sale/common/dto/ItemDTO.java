package com.tadneit.sale.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO extends BaseDTO {
    private UUID id;
    private String brand;
    private String name;
    private String description;
    private Double price;
    private Set<CategoryDTO> categories;
    private Set<FileDTO> images;
}
