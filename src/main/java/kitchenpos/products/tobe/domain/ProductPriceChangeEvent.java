package kitchenpos.products.tobe.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductPriceChangeEvent {
    private final UUID id;
    private final ProductPrice productPrice;

    public ProductPriceChangeEvent(UUID id, ProductPrice productPrice) {
        this.id = id;
        this.productPrice = productPrice;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getProductPrice() {
        return this.productPrice.getPrice();
    }
}
