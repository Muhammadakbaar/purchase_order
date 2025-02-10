package com.backend.purchaseorder.controller;

import com.backend.purchaseorder.dto.item.ItemDTO;
import com.backend.purchaseorder.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ItemDTO sampleItem;

    @BeforeEach
    void setUp() {
        sampleItem = new ItemDTO();
        sampleItem.setId(1);
        sampleItem.setName("Sample Item");
        sampleItem.setDescription("Sample Description");
        sampleItem.setPrice(100);
        sampleItem.setCost(50);
        sampleItem.setCreatedBy("Admin");
        sampleItem.setUpdatedBy("Admin");
    }

    @Test
    void getAllItems_ShouldReturnListOfItems() {
        List<ItemDTO> items = Arrays.asList(sampleItem);
        when(itemService.getAllItems()).thenReturn(items);

        ResponseEntity<List<ItemDTO>> response = itemController.getAllItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void getItemById_ExistingId_ShouldReturnItem() {
        when(itemService.getItemById(1)).thenReturn(Optional.of(sampleItem));

        ResponseEntity<ItemDTO> response = itemController.getItemById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sampleItem.getId(), response.getBody().getId());
        verify(itemService, times(1)).getItemById(1);
    }

    @Test
    void getItemById_NonExistingId_ShouldReturnNotFound() {
        when(itemService.getItemById(99)).thenReturn(Optional.empty());

        ResponseEntity<ItemDTO> response = itemController.getItemById(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemService, times(1)).getItemById(99);
    }

    @Test
    void createItem_ShouldReturnCreatedItem() {
        when(itemService.createItem(any(ItemDTO.class))).thenReturn(sampleItem);

        ResponseEntity<ItemDTO> response = itemController.createItem(sampleItem);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(itemService, times(1)).createItem(any(ItemDTO.class));
    }

    @Test
    void updateItem_ExistingId_ShouldReturnUpdatedItem() {
        when(itemService.updateItem(eq(1), any(ItemDTO.class))).thenReturn(sampleItem);

        ResponseEntity<ItemDTO> response = itemController.updateItem(1, sampleItem);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(itemService, times(1)).updateItem(eq(1), any(ItemDTO.class));
    }

    @Test
    void updateItem_NonExistingId_ShouldReturnNotFound() {
        when(itemService.updateItem(eq(99), any(ItemDTO.class))).thenReturn(null);

        ResponseEntity<ItemDTO> response = itemController.updateItem(99, sampleItem);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemService, times(1)).updateItem(eq(99), any(ItemDTO.class));
    }

    @Test
    void deleteItem_ExistingId_ShouldReturnNoContent() {
        when(itemService.deleteItem(1)).thenReturn(true);

        ResponseEntity<Void> response = itemController.deleteItem(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemService, times(1)).deleteItem(1);
    }

    @Test
    void deleteItem_NonExistingId_ShouldReturnNotFound() {
        when(itemService.deleteItem(99)).thenReturn(false);

        ResponseEntity<Void> response = itemController.deleteItem(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemService, times(1)).deleteItem(99);
    }
}
