package com.backend.purchaseorder.repository;

import com.backend.purchaseorder.entity.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Integer> {
    List<PurchaseOrderDetail> findByPurchaseOrderHeaderId(Integer pohId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PurchaseOrderDetail p WHERE p.purchaseOrderHeader.id = :pohId")
    void deleteByPohId(@Param("pohId") Integer pohId);
    }
