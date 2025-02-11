package com.backend.purchaseorder.controller;

import com.backend.purchaseorder.dto.item.ItemDTO;
import com.backend.purchaseorder.service.ItemService;
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

public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetAllItems() {
        List<ItemDTO> items = Arrays.asList(new ItemDTO(), new ItemDTO());
        when(itemService.getAllItems()).thenReturn(items);

        ResponseEntity<List<ItemDTO>> response = itemController.getAllItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    public void testGetItemById() {
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.getItemById(anyInt())).thenReturn(Optional.of(itemDTO));

        ResponseEntity<ItemDTO> response = itemController.getItemById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemDTO, response.getBody());
    }

    @Test
    public void testGetItemByIdNotFound() {
        when(itemService.getItemById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<ItemDTO> response = itemController.getItemById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateItem() {
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.createItem(any(ItemDTO.class))).thenReturn(itemDTO);

        ResponseEntity<ItemDTO> response = itemController.createItem(itemDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(itemDTO, response.getBody());
    }

    @Test
    public void testUpdateItem() {
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.updateItem(anyInt(), any(ItemDTO.class))).thenReturn(itemDTO);

        ResponseEntity<ItemDTO> response = itemController.updateItem(1, itemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemDTO, response.getBody());
    }

    @Test
    public void testUpdateItemNotFound() {
        when(itemService.updateItem(anyInt(), any(ItemDTO.class))).thenReturn(null);

        ResponseEntity<ItemDTO> response = itemController.updateItem(1, new ItemDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteItem() {
        when(itemService.deleteItem(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = itemController.deleteItem(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteItemNotFound() {
        when(itemService.deleteItem(anyInt())).thenReturn(false);

        ResponseEntity<Void> response = itemController.deleteItem(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
