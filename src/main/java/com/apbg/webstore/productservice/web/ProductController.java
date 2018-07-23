package com.apbg.webstore.productservice.web;

import com.apbg.webstore.productservice.domain.EditProduct;
import com.apbg.webstore.productservice.domain.Inventory;
import com.apbg.webstore.productservice.domain.Product;
import com.apbg.webstore.productservice.services.InventoryFeignService;
import com.apbg.webstore.productservice.services.ProductService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private InventoryFeignService inventoryFeignService;

    @Autowired
    ProductController(ProductService productService, InventoryFeignService inventoryFeignService) {
        this.productService = productService;
        this.inventoryFeignService = inventoryFeignService;
    }

    @GetMapping
    public ResponseEntity<Page<EditProduct>> getPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {

        Page<Product> productPage = productService.getProductPage(page, size);
        List<Inventory> inventories;
        try {
            inventories = inventoryFeignService.getInventories(productPage.getContent());
        } catch (HystrixRuntimeException e) {
            return new ResponseEntity<>(productPage.map(product -> new EditProduct(product, -1)), HttpStatus.OK);
        }
        return new ResponseEntity<>(productPage.map(product -> new EditProduct(product,
                inventories.get(productPage.getContent()
                        .indexOf(product))
                        .getOnHand())),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditProduct> getById(@PathVariable Integer id) {

        Optional<Product> product = productService.getById(id);

        if (!product.isPresent())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        Inventory inventory;

        try {
            inventory = inventoryFeignService.getByProductId(id);
        } catch (HystrixRuntimeException e) {
            return new ResponseEntity<>(new EditProduct(product.get(), -1), HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new EditProduct(product.get(), inventory.getOnHand()),
                HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('product.write')")
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<EditProduct> add(@RequestBody EditProduct editProduct) {
        Product saved = productService.saveProduct(editProduct.toProduct());
        Inventory inventory = Inventory.builder()
                .productId(saved.getId())
                .onHand(editProduct.getInventory())
                .build();
        inventoryFeignService.add(inventory);
        editProduct.setId(saved.getId());
        return new ResponseEntity<>(editProduct, HttpStatus.CREATED);
    }

    @PreAuthorize("#oauth2.hasScope('product.write')")
    @PutMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<EditProduct> update(@PathVariable Integer id, @RequestBody EditProduct editProduct) {
        Optional<Product> oldProduct = productService.getById(id);
        if (!oldProduct.isPresent())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        inventoryFeignService.updateByProductId(editProduct.getId(), Inventory.builder()
                .productId(editProduct.getId())
                .onHand(editProduct.getInventory())
                .build());
        return new ResponseEntity<>(
                new EditProduct(productService.saveProduct(editProduct.toProduct()), editProduct.getInventory()),
                HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('product.write')")
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity deleteById(@PathVariable Integer id) {
        Optional<Product> product = productService.getById(id);
        if (!product.isPresent())
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        inventoryFeignService.deleteByProductId(id);
        productService.delete(product.get());
        return new ResponseEntity(HttpStatus.OK);
    }
}
