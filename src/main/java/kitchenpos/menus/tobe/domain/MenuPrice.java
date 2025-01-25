package kitchenpos.menus.tobe.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuPrice updatePrice(BigDecimal price, BigDecimal totalProductPrice) {
        MenuPrice newMenuPrice = new MenuPrice(price);
        if (newMenuPrice.isTotalProductPriceOver(totalProductPrice)) {
            throw new IllegalArgumentException("총 상품 가격이 메뉴 가격보다 큽니다.");
        }
        return newMenuPrice;
    }

    public boolean isTotalProductPriceOver(BigDecimal totalProductPrice) {
        return this.price.compareTo(totalProductPrice) > 0;
    }
}
