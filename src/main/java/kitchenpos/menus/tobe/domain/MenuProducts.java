package kitchenpos.menus.tobe.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "menu_id",
            nullable = false,
            columnDefinition = "binary(16)",
            foreignKey = @ForeignKey(name = "fk_menu_product_to_menu")
    )
    private List<MenuProduct> menuProducts;

    public static MenuProducts newOne(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new MenuProducts(menuProducts);
    }

    public MenuProducts() {

    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal getAmount() {
        return this.menuProducts.stream()
                .map(MenuProduct::getProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public void updateProductPrice(UUID productId, BigDecimal productPrice) {
        this.menuProducts.stream()
                .filter(it -> it.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수가 없습니다. productId : " + productId))
                .updateProductPrice(productPrice);
    }
}
