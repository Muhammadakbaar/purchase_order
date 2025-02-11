package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.po.PurchaseOrderDetailDTO;
import com.backend.purchaseorder.dto.po.PurchaseOrderHeaderDTO;
import com.backend.purchaseorder.entity.PurchaseOrderDetail;
import com.backend.purchaseorder.entity.PurchaseOrderHeader;
import com.backend.purchaseorder.entity.Item;
import com.backend.purchaseorder.repository.ItemRepository;
import com.backend.purchaseorder.repository.PurchaseOrderDetailRepository;
import com.backend.purchaseorder.repository.PurchaseOrderHeaderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderHeaderRepository poHRepository;
    private final PurchaseOrderDetailRepository poDRepository;
    private final ItemRepository itemRepository;

    public PurchaseOrderService(PurchaseOrderHeaderRepository poHRepository, PurchaseOrderDetailRepository poDRepository, ItemRepository itemRepository) {
        this.poHRepository = poHRepository;
        this.poDRepository = poDRepository;
        this.itemRepository = itemRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<PurchaseOrderHeaderDTO> getAllPOs() {
        List<PurchaseOrderHeader> poHeaders = poHRepository.findAll();
        return poHeaders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public Optional<PurchaseOrderHeaderDTO> getPOById(int id) {
        return poHRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public PurchaseOrderHeaderDTO savePO(PurchaseOrderHeaderDTO poHDTO) {
        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setCreatedDatetime(LocalDateTime.now());
        poH.setDatetime(LocalDateTime.now());
        poH.setCreatedBy(poHDTO.getCreatedBy());
        poH.setUpdatedBy(poHDTO.getCreatedBy());
        poH.setDescription(poHDTO.getDescription());
        poH.setTotalCost(BigDecimal.ZERO);
        poH.setTotalPrice(BigDecimal.ZERO);

        final PurchaseOrderHeader savedPoH = poHRepository.save(poH);

        Map<Integer, Integer> itemQuantityMap = new HashMap<>();
        if (poHDTO.getPoDetails() != null) {
            for (PurchaseOrderDetailDTO dto : poHDTO.getPoDetails()) {
                itemQuantityMap.put(dto.getItemId(), itemQuantityMap.getOrDefault(dto.getItemId(), 0) + dto.getItemQty());
            }
        }

        List<PurchaseOrderDetail> poDetails = itemQuantityMap.entrySet().stream()
            .map(entry -> {
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setItemId(entry.getKey());
                detail.setItemQty(entry.getValue());

                Item item = itemRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Item not found"));
                detail.setItemPrice(BigDecimal.valueOf(item.getPrice()));
                detail.setItemCost(BigDecimal.valueOf(item.getCost()));

                detail.setPurchaseOrderHeader(savedPoH);
                return detail;
            })
            .collect(Collectors.toList());

        poDRepository.saveAll(poDetails);
        savedPoH.setDetails(poDetails);

        BigDecimal totalCost = poDetails.stream()
            .map(detail -> detail.getItemCost().multiply(BigDecimal.valueOf(detail.getItemQty())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPrice = poDetails.stream()
            .map(detail -> detail.getItemPrice().multiply(BigDecimal.valueOf(detail.getItemQty())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        savedPoH.setTotalCost(totalCost);
        savedPoH.setTotalPrice(totalPrice);
        poHRepository.save(savedPoH);

        return convertToDTO(savedPoH);
    }

    @Transactional
    public Optional<PurchaseOrderHeaderDTO> updatePO(int id, PurchaseOrderHeaderDTO poHDTO) {
        return poHRepository.findById(id).map(poH -> {
            poH.setUpdatedBy(poHDTO.getUpdatedBy());
            poH.setDescription(poHDTO.getDescription());
            poH.setDatetime(LocalDateTime.now());

            Map<Integer, Integer> itemQuantityMap = new HashMap<>();
            for (PurchaseOrderDetailDTO dto : poHDTO.getPoDetails()) {
                itemQuantityMap.put(dto.getItemId(), itemQuantityMap.getOrDefault(dto.getItemId(), 0) + dto.getItemQty());
            }

            for (Map.Entry<Integer, Integer> entry : itemQuantityMap.entrySet()) {
                Integer itemId = entry.getKey();
                Integer quantity = entry.getValue();

                Optional<PurchaseOrderDetail> existingDetail = poH.getDetails().stream()
                    .filter(detail -> detail.getItemId().equals(itemId))
                    .findFirst();

                if (existingDetail.isPresent()) {
                    PurchaseOrderDetail detail = existingDetail.get();
                    detail.setItemQty(quantity);
                } else {
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setItemId(itemId);
                    detail.setItemQty(quantity);

                    Item item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Item not found"));
                    detail.setItemPrice(BigDecimal.valueOf(item.getPrice()));
                    detail.setItemCost(BigDecimal.valueOf(item.getCost()));

                    detail.setPurchaseOrderHeader(poH);
                    poH.getDetails().add(detail);
                }
            }

            BigDecimal totalPrice = poH.getDetails().stream()
                .map(detail -> detail.getItemPrice().multiply(BigDecimal.valueOf(detail.getItemQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCost = poH.getDetails().stream()
                .map(detail -> detail.getItemCost().multiply(BigDecimal.valueOf(detail.getItemQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            poH.setTotalPrice(totalPrice);
            poH.setTotalCost(totalCost);

            poHRepository.save(poH);

            return convertToDTO(poH);
        });
    }

    @Transactional
    public boolean deletePO(int id) {
        if (poHRepository.existsById(id)) {
            poDRepository.deleteAllByPurchaseOrderHeaderId(id);
            poHRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PurchaseOrderHeaderDTO convertToDTO(PurchaseOrderHeader poH) {
        PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
        poHDTO.setId(poH.getId());
        poHDTO.setDatetime(poH.getDatetime());
        poHDTO.setDescription(poH.getDescription());
        poHDTO.setTotalPrice(poH.getTotalPrice());
        poHDTO.setTotalCost(poH.getTotalCost());
        poHDTO.setCreatedBy(poH.getCreatedBy());
        poHDTO.setUpdatedBy(poH.getUpdatedBy());

        List<PurchaseOrderDetailDTO> detailDTOs = poH.getDetails().stream()
            .map(detail -> {
                PurchaseOrderDetailDTO detailDTO = new PurchaseOrderDetailDTO();
                detailDTO.setId(detail.getId());
                detailDTO.setPohId(detail.getPurchaseOrderHeader().getId());
                detailDTO.setItemId(detail.getItemId());
                detailDTO.setItemQty(detail.getItemQty());
                detailDTO.setItemCost(detail.getItemCost());
                detailDTO.setItemPrice(detail.getItemPrice());
                return detailDTO;
            })
            .collect(Collectors.toList());

        poHDTO.setPoDetails(detailDTOs);
        return poHDTO;
    }
}
