package com.apbg.webstore.productservice.services;

import com.apbg.webstore.productservice.domain.Inventory;
import com.apbg.webstore.productservice.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory-service", fallback = InventoryFeignServiceFallback.class)
public interface InventoryFeignService {

    @PostMapping(value = "/inventory/get-product-inventory", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Inventory> getInventories(@RequestBody List<Product> products);

    @GetMapping("/inventory/{productId}")
    Inventory getByProductId(@PathVariable("productId") Integer productId);

    @PostMapping("/inventory")
    void add(@RequestBody Inventory inventory);

    @PutMapping("/inventory/{productId}")
    void updateByProductId(@PathVariable("productId") Integer productId, @RequestBody Inventory inventory);

    @DeleteMapping("/inventory/{productId}")
    void deleteByProductId(@PathVariable("productId") Integer productId);
}
