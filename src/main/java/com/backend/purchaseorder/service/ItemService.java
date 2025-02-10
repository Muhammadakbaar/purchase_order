package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.item.ItemDTO;
import com.backend.purchaseorder.entity.Item;
import com.backend.purchaseorder.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<ItemDTO> getAllItems() {
        log.info("Fetching all items");
        return itemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ItemDTO> getItemById(Integer id) {
        log.info("Fetching item by id: {}", id);
        return itemRepository.findById(id)
                .map(this::convertToDTO);
    }

    public ItemDTO createItem(ItemDTO itemDTO) {
        if (itemDTO == null) {
            throw new IllegalArgumentException("ItemDTO cannot be null");
        }
        log.info("Creating item with name: {}", itemDTO.getName());
        Item item = convertToEntity(itemDTO);
        item.setCreatedDatetime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        return convertToDTO(savedItem);
    }

    public ItemDTO updateItem(Integer id, ItemDTO itemDTO) {
        if (itemDTO == null) {
            throw new IllegalArgumentException("ItemDTO cannot be null");
        }
        log.info("Updating item with id: {}", id);
        return itemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(itemDTO.getName());
                    existingItem.setDescription(itemDTO.getDescription());
                    existingItem.setPrice(itemDTO.getPrice());
                    existingItem.setCost(itemDTO.getCost());
                    existingItem.setUpdatedBy(itemDTO.getUpdatedBy());
                    existingItem.setUpdatedDatetime(LocalDateTime.now());

                    Item updatedItem = itemRepository.save(existingItem);
                    return convertToDTO(updatedItem);
                }).orElse(null);
    }

    public boolean deleteItem(Integer id) {
        log.info("Deleting item with id: {}", id);
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ItemDTO convertToDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .cost(item.getCost())
                .createdBy(item.getCreatedBy())
                .updatedBy(item.getUpdatedBy())
                .build();
    }

    private Item convertToEntity(ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .price(itemDTO.getPrice())
                .cost(itemDTO.getCost())
                .createdBy(itemDTO.getCreatedBy())
                .updatedBy(itemDTO.getUpdatedBy())
                .build();
    }
}