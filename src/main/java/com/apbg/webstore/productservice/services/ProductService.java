package com.apbg.webstore.productservice.services;

import com.apbg.webstore.productservice.domain.Product;
import com.apbg.webstore.productservice.repository.ProductRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @HystrixCommand(fallbackMethod = "getProductPageFallback")
    public Page<Product> getProductPage(Integer page, Integer size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Product> getProductPageFallback(Integer page, Integer size) {
        return null;
    }

    @HystrixCommand(fallbackMethod = "getByIdFallback")
    public Optional<Product> getById(Integer id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getByIdFallback(Integer id) {
        return Optional.empty();
    }

    @HystrixCommand(fallbackMethod = "saveProductFallback")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product saveProductFallback(Product product) {
        return null;
    }

    @HystrixCommand(fallbackMethod = "deleteFallback")
    public void delete(Product product) {
        productRepository.delete(product);
    }

    public void deleteFallback(Product product) {
    }
}