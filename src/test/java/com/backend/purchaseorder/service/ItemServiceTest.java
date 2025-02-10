package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.item.ItemDTO;
import com.backend.purchaseorder.entity.Item;
import com.backend.purchaseorder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;
    private ItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .price(100)
                .cost(50)
                .createdBy("User1")
                .updatedBy("User1")
                .createdDatetime(LocalDateTime.now())
                .build();

        itemDTO = ItemDTO.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .price(100)
                .cost(50)
                .createdBy("User1")
                .updatedBy("User1")
                .build();
    }

    @Test
    void testGetAllItems() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        List<ItemDTO> items = itemService.getAllItems();

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetItemById() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Optional<ItemDTO> foundItem = itemService.getItemById(1);

        assertTrue(foundItem.isPresent());
        assertEquals("Item1", foundItem.get().getName());
        verify(itemRepository, times(1)).findById(1);
    }

    @Test
    void testCreateItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDTO createdItem = itemService.createItem(itemDTO);

        assertNotNull(createdItem);
        assertEquals("Item1", createdItem.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testUpdateItem() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDTO updatedItem = itemService.updateItem(1, itemDTO);

        assertNotNull(updatedItem);
        assertEquals("Item1", updatedItem.getName());
        verify(itemRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testDeleteItem() {
        when(itemRepository.existsById(1)).thenReturn(true);
        doNothing().when(itemRepository).deleteById(1);

        boolean isDeleted = itemService.deleteItem(1);

        assertTrue(isDeleted);
        verify(itemRepository, times(1)).existsById(1);
        verify(itemRepository, times(1)).deleteById(1);
    }
}