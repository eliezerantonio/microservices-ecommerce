package com.ecommerce.inventory_service.service.impl;

import com.ecommerce.inventory_service.dto.InventoryRequest;
import com.ecommerce.inventory_service.dto.InventoryResponse;
import com.ecommerce.inventory_service.mapper.InventoryMapper;
import com.ecommerce.inventory_service.model.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.ecommerce.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class InventoryServiceImpl implements InventoryService {


    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;


    @Value("${inventory.allow-backorders:false}")
    private boolean allowBackorders;


    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(String sku, Integer quantity) {


        if (allowBackorders) {
            log.warn("BACKORDER MODE ACTIVE: Authorizing stock to SKU: {}", sku);
            return false;
        }
        return inventoryRepository.findBySku(sku).map(inventory -> inventory.getQuantity() >= quantity).orElse(false);
    }

    @Override
    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {

        boolean exists = inventoryRepository.existsBySku(inventoryRequest.getSku());

        if (exists) {
            throw new RuntimeException("Inventory already exists: " + inventoryRequest.getSku());
        }

        Inventory inventory = inventoryMapper.toModel(inventoryRequest);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(savedInventory);

    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream().map(inventoryMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Long id, InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found: " + id));

        inventory.setSku(inventoryRequest.getSku());
        inventory.setQuantity(inventoryRequest.getQuantity());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        log.info("Inventory updated: {}", id);
        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new RuntimeException("Inventory not found: " + id);
        }
        inventoryRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void reduceStock(String sku, Integer quantity) {

        var inventory = inventoryRepository.findBySku(sku).orElseThrow(() -> new RuntimeException("Product not found: " + sku));


        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Quantity exceeded for: " + sku);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);

        inventoryRepository.save(inventory);
    }
}
