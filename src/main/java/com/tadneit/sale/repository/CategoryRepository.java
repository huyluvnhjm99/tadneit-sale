package com.tadneit.sale.repository;

import com.tadneit.sale.common.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends BaseRepository<Category, UUID> {
}

