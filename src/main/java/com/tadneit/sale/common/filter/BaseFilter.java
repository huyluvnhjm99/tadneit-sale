package com.tadneit.sale.common.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseFilter {
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
    private LocalDateTime updatedDateFrom;
    private LocalDateTime updatedDateTo;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");

    private Integer page;
    private Integer size;
    private List<String> sort;

    public int getPageOrDefault() {
        return (page == null || page < 0) ? DEFAULT_PAGE_NUMBER : page;
    }

    public int getSizeOrDefault() {
        return (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }

    public org.springframework.data.domain.Sort getSortOrDefault() {
        if (this.sort == null || this.sort.isEmpty()) {
            return DEFAULT_SORT;
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortParam : this.sort) {
            if (sortParam == null || sortParam.trim().isEmpty()) {
                continue;
            }
            String[] parts = sortParam.split(",");
            String property = parts[0].trim();
            if (property.isEmpty()) {
                continue;
            }
            Sort.Direction direction = Sort.Direction.ASC;
            if (parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }
            orders.add(new Sort.Order(direction, property));
        }
        return orders.isEmpty() ? DEFAULT_SORT : Sort.by(orders);
    }
}
