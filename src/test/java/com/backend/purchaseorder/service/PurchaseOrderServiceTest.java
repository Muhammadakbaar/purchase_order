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

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
            // Arrange
            PurchaseOrderHeader poH = new PurchaseOrderHeader();
            poH.setId(1);
            poH.setDatetime(LocalDateTime.now());
            poH.setDescription("Test PO");
            poH.setTotalPrice(BigDecimal.valueOf(100.0));
            poH.setTotalCost(BigDecimal.valueOf(80.0));
            poH.setCreatedBy("admin");
            poH.setUpdatedBy("admin");

            when(poHRepository.findAll()).thenReturn(Collections.singletonList(poH));

            // Act
            List<PurchaseOrderHeaderDTO> result = purchaseOrderService.getAllPOs();

            // Assert
            assertEquals(1, result.size());
            assertEquals(1, result.get(0).getId());
            verify(poHRepository, times(1)).findAll();
        }

        @Test
        void testGetPOById_Found() {
            // Arrange
            PurchaseOrderHeader poH = new PurchaseOrderHeader();
            poH.setId(1);
            poH.setDatetime(LocalDateTime.now());
            poH.setDescription("Test PO");
            poH.setTotalPrice(BigDecimal.valueOf(100.0));
            poH.setTotalCost(BigDecimal.valueOf(80.0));
            poH.setCreatedBy("admin");
            poH.setUpdatedBy("admin");

            when(poHRepository.findById(1)).thenReturn(Optional.of(poH));

            // Act
            Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.getPOById(1);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(1, result.get().getId());
            verify(poHRepository, times(1)).findById(1);
        }

        @Test
        void testGetPOById_NotFound() {
            // Arrange
            when(poHRepository.findById(1)).thenReturn(Optional.empty());

            // Act
            Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.getPOById(1);

            // Assert
            assertFalse(result.isPresent());
            verify(poHRepository, times(1)).findById(1);
        }

        @Test
        void testSavePO() {
            // Arrange
            PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
            poHDTO.setCreatedBy("admin");
            poHDTO.setUpdatedBy("admin");
            poHDTO.setPoDetails(Collections.singletonList(new PurchaseOrderDetailDTO()));

            PurchaseOrderHeader poH = new PurchaseOrderHeader();
            poH.setId(1);
            poH.setDatetime(LocalDateTime.now());
            poH.setCreatedBy("admin");
            poH.setUpdatedBy("admin");

            when(poHRepository.save(any(PurchaseOrderHeader.class))).thenReturn(poH);

            // Act
            PurchaseOrderHeaderDTO result = purchaseOrderService.savePO(poHDTO);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getId());
            verify(poHRepository, times(1)).save(any(PurchaseOrderHeader.class));
        }

        @Test
        void testUpdatePO_Found() {
            // Arrange
            PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
            poHDTO.setUpdatedBy("admin");
            poHDTO.setPoDetails(Collections.singletonList(new PurchaseOrderDetailDTO()));

            PurchaseOrderHeader poH = new PurchaseOrderHeader();
            poH.setId(1);
            poH.setDatetime(LocalDateTime.now());
            poH.setUpdatedBy("admin");

            when(poHRepository.findById(1)).thenReturn(Optional.of(poH));
            when(poHRepository.save(any(PurchaseOrderHeader.class))).thenReturn(poH);

            // Act
            Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.updatePO(1, poHDTO);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(1, result.get().getId());
            verify(poHRepository, times(1)).findById(1);
            verify(poHRepository, times(1)).save(any(PurchaseOrderHeader.class));
        }

        @Test
        void testUpdatePO_NotFound() {
            // Arrange
            PurchaseOrderHeaderDTO poHDTO = new PurchaseOrderHeaderDTO();
            poHDTO.setUpdatedBy("admin");
            poHDTO.setPoDetails(Collections.singletonList(new PurchaseOrderDetailDTO()));

            when(poHRepository.findById(1)).thenReturn(Optional.empty());

            // Act
            Optional<PurchaseOrderHeaderDTO> result = purchaseOrderService.updatePO(1, poHDTO);

            // Assert
            assertFalse(result.isPresent());
            verify(poHRepository, times(1)).findById(1);
            verify(poHRepository, never()).save(any(PurchaseOrderHeader.class));
        }

        @Test
        void testDeletePO_Success() {
            // Arrange
            when(poHRepository.existsById(1)).thenReturn(true);

            // Act
            boolean result = purchaseOrderService.deletePO(1);

            // Assert
            assertTrue(result);
            verify(poHRepository, times(1)).existsById(1);
            verify(poDRepository, times(1)).deleteByPohId(1);
            verify(poHRepository, times(1)).deleteById(1);
        }

        @Test
        void testDeletePO_NotFound() {
            // Arrange
            when(poHRepository.existsById(1)).thenReturn(false);

            // Act
            boolean result = purchaseOrderService.deletePO(1);

            // Assert
            assertFalse(result);
            verify(poHRepository, times(1)).existsById(1);
            verify(poDRepository, never()).deleteByPohId(1);
            verify(poHRepository, never()).deleteById(1);
        }
    }