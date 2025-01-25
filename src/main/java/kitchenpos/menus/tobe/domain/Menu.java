package kitchenpos.menus.tobe.domain;

import kitchenpos.menus.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

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

    public static Menu from(String name, BigDecimal price, boolean displayed, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(UUID.randomUUID(), name, new MenuPrice(price), menuGroup, displayed, menuProducts);
    }

    public Menu() {
    }

    public Menu(UUID id, String name, MenuPrice menuPrice, MenuGroup menuGroup, boolean displayed, MenuProducts menuProducts) {
        if (menuPrice.isTotalProductPriceOver(menuProducts.getTotalProductPrice())) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroup = menuGroup;
        this.displayed = displayed;
        this.menuProducts = menuProducts;
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
        return menuGroup.getId();
    }

    public void setMenuGroupId(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
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
