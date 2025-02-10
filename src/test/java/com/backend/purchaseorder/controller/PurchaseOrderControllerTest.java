package com.backend.purchaseorder.controller;


import com.backend.purchaseorder.dto.po.PurchaseOrderHeaderDTO;
import com.backend.purchaseorder.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




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
    public void testGetAllPOs() throws Exception {
        List<PurchaseOrderHeaderDTO> poList = Arrays.asList(new PurchaseOrderHeaderDTO(), new PurchaseOrderHeaderDTO());
        when(poService.getAllPOs()).thenReturn(poList);

        mockMvc.perform(get("/pos"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPOById() throws Exception {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.getPOById(anyInt())).thenReturn(Optional.of(po));

        mockMvc.perform(get("/pos/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPOByIdNotFound() throws Exception {
        when(poService.getPOById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/pos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePO() throws Exception {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.savePO(any(PurchaseOrderHeaderDTO.class))).thenReturn(po);

        mockMvc.perform(post("/pos")
                .contentType("application/json")
                .content("{\"field\":\"value\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePO() throws Exception {
        PurchaseOrderHeaderDTO po = new PurchaseOrderHeaderDTO();
        when(poService.updatePO(anyInt(), any(PurchaseOrderHeaderDTO.class))).thenReturn(Optional.of(po));

        mockMvc.perform(put("/pos/1")
                .contentType("application/json")
                .content("{\"field\":\"value\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePONotFound() throws Exception {
        when(poService.updatePO(anyInt(), any(PurchaseOrderHeaderDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/pos/1")
                .contentType("application/json")
                .content("{\"field\":\"value\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePO() throws Exception {
        when(poService.deletePO(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/pos/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePONotFound() throws Exception {
        when(poService.deletePO(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/pos/1"))
                .andExpect(status().isNotFound());
    }
}