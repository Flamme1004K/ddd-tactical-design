package kitchenpos.menus.application;

import kitchenpos.menus.domain.Menu;
import kitchenpos.menus.domain.MenuGroupRepository;
import kitchenpos.menus.domain.MenuRepository;
import kitchenpos.menus.infra.listener.ProductPriceChangeListener;
import kitchenpos.products.application.FakePurgomalumClient;
import kitchenpos.products.application.InMemoryProductRepository;
import kitchenpos.products.tobe.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;

import static kitchenpos.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;


class ProductPriceChangeListenerTest {

    private ProductPriceChangeListener productPriceChangeListener;
    private MenuRepository menuRepository;
    private MenuGroupRepository menuGroupRepository;
    private ProductRepository productRepository;
    private PurgomalumClient purgomalumClient;
    private MenuService menuService;
    private Product product;

    @BeforeEach
    void setUp() {
        menuRepository = new InMemoryMenuRepository();
        menuGroupRepository = new InMemoryMenuGroupRepository();
        productRepository = new InMemoryProductRepository();
        purgomalumClient = new FakePurgomalumClient();
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, purgomalumClient);
        productPriceChangeListener = new ProductPriceChangeListener(menuService);
        product = productRepository.save(product("후라이드", 16_000L));
    }

    @DisplayName("상품의 가격이 변경될 때 메뉴의 가격이 메뉴에 속한 상품 금액의 합보다 크면 메뉴가 숨겨진다.")
    @Test
    void changePriceInMenu() {
        final Menu menu = menuRepository.save(menu(19_000L, menuProduct(product, 2L)));
        productPriceChangeListener.listen(new ProductPriceChangeEvent(product.getId(), new ProductPrice(BigDecimal.valueOf(19_000L))));
        assertThat(menu.isDisplayed()).isFalse();
    }

}
