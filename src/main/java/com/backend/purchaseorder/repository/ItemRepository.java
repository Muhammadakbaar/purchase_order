package com.backend.purchaseorder.repository;

import com.backend.purchaseorder.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
