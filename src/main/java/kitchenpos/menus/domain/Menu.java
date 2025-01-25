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

    @Embedded
    private MenuProducts menuProducts;

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
        return this.menuProducts.getMenuProducts();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public UUID getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final UUID menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void updateMenuProductPrice(UUID productId, BigDecimal productPrice) {
        this.menuProducts.updateProductPrice(productId, productPrice);
        if (this.menuPrice.isTotalProductPriceOver(this.menuProducts.getTotalProductPrice())) {
            this.displayed = false;
        }
    }

    public void updateMenuPrice(BigDecimal price) {
        this.menuPrice = menuPrice.updatePrice(price, this.menuProducts.getTotalProductPrice());
    }

    public void display() {
        if (this.menuPrice.isTotalProductPriceOver(this.menuProducts.getTotalProductPrice())) {
            throw new IllegalStateException("상품 총 가격이 메뉴의 가격보다 높아 전시할 수 없습니다.");
        }
        this.displayed = true;
    }

    public void hide() {
        this.displayed = false;
    }
}
