package com.apbg.webstore.productservice.services;

import com.apbg.webstore.productservice.domain.Inventory;
import com.apbg.webstore.productservice.domain.Product;
import com.apbg.webstore.productservice.exception.ServiceTimeoutException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryFeignServiceFallback implements InventoryFeignService {

    @Override
    public List<Inventory> getInventories(List<Product> products) {
        throw new ServiceTimeoutException();
    }

    @Override
    public Inventory getByProductId(Integer productId) {
        throw new ServiceTimeoutException();
    }

    @Override
    public void add(Inventory inventory) {
        throw new ServiceTimeoutException();
    }

    @Override
    public void updateByProductId(Integer productId, Inventory inventory) {
        throw new ServiceTimeoutException();
    }

    @Override
    public void deleteByProductId(Integer productId) {
        throw new ServiceTimeoutException();
    }
}