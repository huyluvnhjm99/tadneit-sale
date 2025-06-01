package com.tadneit.sale.common.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFilter extends BaseFilter {
    private String name;
    private String description;
    private String categoryName;
    private UUID categoryId;
    private String brand;
    private Double priceFrom;
    private Double priceTo;
}
