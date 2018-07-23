package com.apbg.webstore.productservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
    private Integer id;
    private Integer productId;
    private Integer onHand;
}
