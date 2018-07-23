package com.apbg.webstore.productservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EditProduct extends Product {
    private Integer inventory;

    public EditProduct() {
        super();
    }

    public EditProduct(Product product, Integer inventory) {
        super();
        this.setId(product.getId());
        this.setName(product.getName());
        this.setImage(product.getImage());
        this.setPrice(product.getPrice());
        this.setDescription(product.getDescription());
        this.inventory = inventory;
    }

    public Product toProduct() {
        return Product.builder()
                .id(getId())
                .name(getName())
                .image(getImage())
                .description(getDescription())
                .price(getPrice())
                .build();
    }
}
