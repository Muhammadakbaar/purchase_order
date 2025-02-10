package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.po.PurchaseOrderDetailDTO;
import com.backend.purchaseorder.dto.po.PurchaseOrderHeaderDTO;
import com.backend.purchaseorder.entity.PurchaseOrderHeader;
import com.backend.purchaseorder.repository.PurchaseOrderDetailRepository;
import com.backend.purchaseorder.repository.PurchaseOrderHeaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderHeaderRepository poHRepository;

    @Mock
    private PurchaseOrderDetailRepository poDRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPOs() {
        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setId(1);
        poH.setDatetime(LocalDateTime.now());
        poH.setDescription("Test PO");
        poH.setTotalPrice(BigDecimal.valueOf(100.0));
        poH.setTotalCost(BigDecimal.valueOf(80.0));
        poH.setCreatedBy("admin");
        poH.setUpdatedBy("admin");

        when(poHRepository.findAll()).thenReturn(Collections.singletonList(poH));

        List<PurchaseOrderHeaderDTO> result = purchaseOrderService.getAllPOs();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        verify(poHRepository, times(1)).findAll();
    }

    @Test
    void testGetPOById_Found() {
        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setId(1);
        poH.setDatetime(LocalDateTime.now());
        poH.setDescription("Test PO");
        poH.setTotalPrice(BigDecimal.valueOf(100.0));
        poH.setTotalCost(BigDecimal.valueOf(80.0));
        poH.setCreatedBy("admin");
        poH.setUpdatedBy("admin");

        when(poHRepository.findById(1)).thenReturn(Optional.of(poH));

        Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.getPOById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(poHRepository, times(1)).findById(1);
    }

    @Test
    void testGetPOById_NotFound() {
        when(poHRepository.findById(1)).thenReturn(Optional.empty());

        Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.getPOById(1);

        assertFalse(result.isPresent());
        verify(poHRepository, times(1)).findById(1);
    }

    @Test
    void testSavePO() {
        PurchaseOrderDetailDTO poDetailDTO = new PurchaseOrderDetailDTO();
        poDetailDTO.setItemQty(2);
        poDetailDTO.setItemPrice(BigDecimal.valueOf(50.0));
        poDetailDTO.setItemCost(BigDecimal.valueOf(40.0));

        PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
        poHDTO.setCreatedBy("admin");
        poHDTO.setUpdatedBy("admin");
        poHDTO.setDescription("Test PO");
        poHDTO.setPoDetails(Collections.singletonList(poDetailDTO));

        PurchaseOrderHeader savedPoH = new PurchaseOrderHeader();
        savedPoH.setId(1);
        savedPoH.setDatetime(LocalDateTime.now());
        savedPoH.setCreatedBy("admin");
        savedPoH.setUpdatedBy("admin");
        savedPoH.setDescription("Test PO");
        savedPoH.setTotalPrice(BigDecimal.valueOf(80.0));
        savedPoH.setTotalCost(BigDecimal.valueOf(100.0));

        when(poHRepository.save(any(PurchaseOrderHeader.class))).thenReturn(savedPoH);
        when(poDRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        PurchaseOrderHeaderDTO result = purchaseOrderService.savePO(poHDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(BigDecimal.valueOf(100.0), result.getTotalCost());
        assertTrue(BigDecimal.valueOf(80.0).compareTo(result.getTotalPrice()) == 0);

        verify(poHRepository, times(1)).save(any(PurchaseOrderHeader.class));
        verify(poDRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdatePO_Found() {
        PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
        poHDTO.setUpdatedBy("admin");
        poHDTO.setDescription("Updated PO");
        poHDTO.setPoDetails(Collections.singletonList(new PurchaseOrderDetailDTO()));

        PurchaseOrderHeader poH = new PurchaseOrderHeader();
        poH.setId(1);
        poH.setDatetime(LocalDateTime.now());
        poH.setUpdatedBy("admin");
        poH.setDescription("Updated PO");
        poH.setTotalPrice(BigDecimal.valueOf(100.0));
        poH.setTotalCost(BigDecimal.valueOf(80.0));

        when(poHRepository.findById(1)).thenReturn(Optional.of(poH));
        when(poHRepository.save(any(PurchaseOrderHeader.class))).thenReturn(poH);

        Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.updatePO(1, poHDTO);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(poHRepository, times(1)).findById(1);
        verify(poHRepository, times(1)).save(any(PurchaseOrderHeader.class));
    }

    @Test
    void testUpdatePO_NotFound() {
        PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
        poHDTO.setUpdatedBy("admin");
        poHDTO.setDescription("Updated PO");
        poHDTO.setPoDetails(Collections.singletonList(new PurchaseOrderDetailDTO()));

        when(poHRepository.findById(1)).thenReturn(Optional.empty());

        Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.updatePO(1, poHDTO);

        assertFalse(result.isPresent());
        verify(poHRepository, times(1)).findById(1);
        verify(poHRepository, never()).save(any(PurchaseOrderHeader.class));
    }

    @Test
    void testDeletePO_Success() {
        when(poHRepository.existsById(1)).thenReturn(true);

        boolean result = purchaseOrderService.deletePO(1);

        assertTrue(result);
        verify(poHRepository, times(1)).existsById(1);
        verify(poDRepository, times(1)).deleteAllByPurchaseOrderHeaderId(1);
        verify(poHRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePO_NotFound() {
        when(poHRepository.existsById(1)).thenReturn(false);

        boolean result = purchaseOrderService.deletePO(1);

        assertFalse(result);
        verify(poHRepository, times(1)).existsById(1);
        verify(poDRepository, never()).deleteAllByPurchaseOrderHeaderId(1);
        verify(poHRepository, never()).deleteById(1);
    }
}
