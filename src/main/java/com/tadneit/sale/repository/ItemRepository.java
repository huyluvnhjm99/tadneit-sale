package com.tadneit.sale.repository;

import com.tadneit.sale.common.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends BaseRepository<Item, UUID> {
}

