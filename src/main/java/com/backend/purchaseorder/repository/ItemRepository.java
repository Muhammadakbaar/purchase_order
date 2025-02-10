package com.backend.purchaseorder.repository;

import com.backend.purchaseorder.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : purchaseorder
 * User: Muhammad Akbar
 * GitHub: muhammadakbaar
 * Date: 2/9/25
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
