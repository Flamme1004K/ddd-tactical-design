package kitchenpos.menus.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Table(name = "menu")
@Entity
public class Menu {
    @Column(name = "id", columnDefinition = "binary(16)")
    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private MenuPrice menuPrice;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "menu_group_id",
            columnDefinition = "binary(16)",
            foreignKey = @ForeignKey(name = "fk_menu_to_menu_group")
    )
    private MenuGroup menuGroup;

    @Column(name = "displayed", nullable = false)
    private boolean displayed;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "menu_id",
            nullable = false,
            columnDefinition = "binary(16)",
            foreignKey = @ForeignKey(name = "fk_menu_product_to_menu")
    )
    private List<MenuProduct> menuProducts;

    @Transient
    private UUID menuGroupId;

    public Menu() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return menuPrice.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.menuPrice = new MenuPrice(price);
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(final boolean displayed) {
        this.displayed = displayed;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public UUID getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final UUID menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void updateMenuProductPrice(UUID productId, BigDecimal productPrice) {
        this.menuProducts.stream()
                .filter(it -> it.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수가 없습니다. productId : " + productId))
                .updateProductPrice(productPrice);

        var totalProductPrice = getTotalProductPrice();
        if (this.menuPrice.isTotalProductPriceOver(totalProductPrice)) {
            this.displayed = false;
        }
    }

    public BigDecimal getTotalProductPrice() {
        return menuProducts.stream()
                .map(MenuProduct::getProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateMenuPrice(BigDecimal price) {
        this.menuPrice = menuPrice.updatePrice(price, this.getTotalProductPrice());
    }

    public void display() {
        if (this.menuPrice.isTotalProductPriceOver(this.getTotalProductPrice())) {
            throw new IllegalStateException("상품 총 가격이 메뉴의 가격보다 높아 전시할 수 없습니다.");
        }
        this.displayed = true;
    }

    public void hide() {
        this.displayed = false;
    }
}
