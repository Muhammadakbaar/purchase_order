package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.po.PurchaseOrderDetailDTO;
import com.backend.purchaseorder.dto.po.PurchaseOrderHeaderDTO;
import com.backend.purchaseorder.entity.PurchaseOrderDetail;
import com.backend.purchaseorder.entity.PurchaseOrderHeader;
import com.backend.purchaseorder.repository.PurchaseOrderDetailRepository;
import com.backend.purchaseorder.repository.PurchaseOrderHeaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderHeaderRepository poHRepository;
    private final PurchaseOrderDetailRepository poDRepository;

    public PurchaseOrderService(PurchaseOrderHeaderRepository poHRepository,
            PurchaseOrderDetailRepository poDRepository) {
        this.poHRepository = poHRepository;
        this.poDRepository = poDRepository;
    }

    public List<PurchaseOrderHeaderDTO> getAllPOs() {
        return poHRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PurchaseOrderHeaderDTO> getPOById(Integer id) {
        return poHRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public PurchaseOrderHeaderDTO savePO(PurchaseOrderHeaderDTO dto) {
        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setCreatedBy(dto.getCreatedBy());
        poH.setUpdatedBy(dto.getUpdatedBy());
        poH.setDatetime(LocalDateTime.now()); // Set the datetime field
        poH.setCreatedDatetime(LocalDateTime.now()); // Set the created_datetime field
        poH.setDescription(dto.getDescription()); // Set the description field

        List<PurchaseOrderDetail> details = dto.getPoDetails().stream().map(detail -> {
            PurchaseOrderDetail poD = new PurchaseOrderDetail();
            poD.setItemId(detail.getItemId());
            poD.setItemQty(detail.getItemQty());
            poD.setItemCost(detail.getItemCost());
            poD.setItemPrice(detail.getItemPrice());
            poD.setCreatedDatetime(LocalDateTime.now()); // Set the created_datetime field
            poD.setPurchaseOrderHeader(poH);
            return poD;
        }).collect(Collectors.toList());

        poH.setDetails(details);

        // Calculate total cost and total price
        double totalCost = details.stream().mapToDouble(d -> d.getItemQty() * d.getItemCost().doubleValue()).sum();
        double totalPrice = details.stream().mapToDouble(d -> d.getItemQty() * d.getItemPrice().doubleValue()).sum();

        poH.setTotalCost(BigDecimal.valueOf(totalCost));
        poH.setTotalPrice(BigDecimal.valueOf(totalPrice));

        return convertToDTO(poHRepository.save(poH));
    }

    @Transactional
    public Optional<PurchaseOrderHeaderDTO> updatePO(Integer id, PurchaseOrderHeaderDTO dto) {
        return poHRepository.findById(id).map(poH -> {
            poH.setUpdatedBy(dto.getUpdatedBy());
            poH.setDatetime(LocalDateTime.now());
            poH.setUpdatedDatetime(LocalDateTime.now()); // Set the updated_datetime field
            poH.setDescription(dto.getDescription()); // Set the description field

            // Add new details without clearing existing ones
            List<PurchaseOrderDetail> existingDetails = poH.getDetails();
            List<PurchaseOrderDetail> newDetails = dto.getPoDetails().stream().map(detail -> {
                PurchaseOrderDetail poD;
                if (detail.getId() != null) {
                    poD = poDRepository.findById(detail.getId()).orElse(new PurchaseOrderDetail());
                } else {
                    poD = new PurchaseOrderDetail();
                }
                poD.setItemId(detail.getItemId());
                poD.setItemQty(detail.getItemQty());
                poD.setItemCost(detail.getItemCost());
                poD.setItemPrice(detail.getItemPrice());
                poD.setCreatedDatetime(LocalDateTime.now()); // Set the created_datetime field
                poD.setPurchaseOrderHeader(poH);
                return poD;
            }).collect(Collectors.toList());

            existingDetails.addAll(newDetails);

            // Calculate total cost and total price
            double totalCost = existingDetails.stream().mapToDouble(d -> d.getItemQty() * d.getItemCost().doubleValue()).sum();
            double totalPrice = existingDetails.stream().mapToDouble(d -> d.getItemQty() * d.getItemPrice().doubleValue()).sum();

            poH.setTotalCost(BigDecimal.valueOf(totalCost));
            poH.setTotalPrice(BigDecimal.valueOf(totalPrice));

            return convertToDTO(poHRepository.save(poH));
        });
    }

    @Transactional
    public boolean deletePO(Integer id) {
        if (poHRepository.existsById(id)) {
            poDRepository.deleteByPohId(id);
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
        poHDTO.setPoDetails(poDRepository.findByPurchaseOrderHeaderId(poH.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        return poHDTO;
    }

    private PurchaseOrderDetailDTO convertToDTO(PurchaseOrderDetail poD) {
        PurchaseOrderDetailDTO poDDTO = new PurchaseOrderDetailDTO();
        poDDTO.setId(poD.getId());
        poDDTO.setPohId(poD.getPurchaseOrderHeader().getId()); // Set the pohId field
        poDDTO.setItemId(poD.getItemId());
        poDDTO.setItemQty(poD.getItemQty());
        poDDTO.setItemCost(poD.getItemCost());
        poDDTO.setItemPrice(poD.getItemPrice());
        return poDDTO;
    }

    private PurchaseOrderHeader convertToEntity(PurchaseOrderHeaderDTO poHDTO) {
        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setId(poHDTO.getId());
        poH.setDatetime(poHDTO.getDatetime());
        poH.setDescription(poHDTO.getDescription());
        poH.setTotalPrice(poHDTO.getTotalPrice());
        poH.setTotalCost(poHDTO.getTotalCost());
        poH.setCreatedBy(poHDTO.getCreatedBy());
        poH.setUpdatedBy(poHDTO.getUpdatedBy());
        return poH;
    }

    private PurchaseOrderDetail convertToEntity(PurchaseOrderDetailDTO poDDTO) {
        PurchaseOrderDetail poD = new PurchaseOrderDetail();
        poD.setId(poDDTO.getId());
        poD.setItemId(poDDTO.getItemId());
        poD.setItemQty(poDDTO.getItemQty());
        poD.setItemCost(poDDTO.getItemCost());
        poD.setItemPrice(poDDTO.getItemPrice());
        poD.setCreatedDatetime(LocalDateTime.now()); // Set the created_datetime field
        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setId(poDDTO.getPohId());
        poD.setPurchaseOrderHeader(poH);
        return poD;
    }
}