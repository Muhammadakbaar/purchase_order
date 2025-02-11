package com.backend.purchaseorder.controller;

import com.backend.purchaseorder.dto.po.PurchaseOrderHeaderDTO;
import com.backend.purchaseorder.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PurchaseOrderControllerTest {

    @Mock
    private PurchaseOrderService poService;

    @InjectMocks
    private PurchaseOrderController poController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(poController).build();
    }

    @Test
    public void testGetAllPOs() {
        PurchaseOrderHeaderDTO po1 = new PurchaseOrderHeaderDTO();
        PurchaseOrderHeaderDTO po2 = new PurchaseOrderHeaderDTO();
        List<PurchaseOrderHeaderDTO> poList = Arrays.asList(po1, po2);

        when(poService.getAllPOs()).thenReturn(poList);

        List<PurchaseOrderHeaderDTO> result = poController.getAllPOs();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetPOById() {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.getPOById(anyInt())).thenReturn(Optional.of(po));

        ResponseEntity<PurchaseOrderHeaderDTO> response = poController.getPOById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(po, response.getBody());
    }

    @Test
    public void testGetPOById_NotFound() {
        when(poService.getPOById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<PurchaseOrderHeaderDTO> response = poController.getPOById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreatePO() {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.savePO(any(PurchaseOrderHeaderDTO.class))).thenReturn(po);

        ResponseEntity<PurchaseOrderHeaderDTO> response = poController.createPO(po);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(po, response.getBody());
    }

    @Test
    public void testUpdatePO() {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.updatePO(anyInt(), any(PurchaseOrderHeaderDTO.class))).thenReturn(Optional.of(po));

        ResponseEntity<PurchaseOrderHeaderDTO> response = poController.updatePO(1, po);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(po, response.getBody());
    }

    @Test
    public void testUpdatePO_NotFound() {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.updatePO(anyInt(), any(PurchaseOrderHeaderDTO.class))).thenReturn(Optional.empty());

        ResponseEntity<PurchaseOrderHeaderDTO> response = poController.updatePO(1, po);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeletePO() {
        when(poService.deletePO(anyInt())).thenReturn(true);

        ResponseEntity<String> response = poController.deletePO(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted successfully", response.getBody());
    }

    @Test
    public void testDeletePO_NotFound() {
        when(poService.deletePO(anyInt())).thenReturn(false);

        ResponseEntity<String> response = poController.deletePO(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("PO not found", response.getBody());
    }
}