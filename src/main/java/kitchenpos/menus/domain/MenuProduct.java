package kitchenpos.menus.domain;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "menu_product")
@Entity
public class MenuProduct {
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "price")
    private BigDecimal productPrice;

    public MenuProduct() {
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public BigDecimal getProductPrice() {
        return this.productPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public void setProductId(final UUID productId) {
        this.productId = productId;
    }

    public void updateProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
}
