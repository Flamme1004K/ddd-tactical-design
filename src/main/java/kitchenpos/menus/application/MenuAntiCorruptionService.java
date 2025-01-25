package kitchenpos.menus.application;

import kitchenpos.menus.application.dto.MenuCreateProductRequest;
import kitchenpos.menus.tobe.domain.MenuProduct;
import kitchenpos.menus.tobe.domain.MenuProducts;
import kitchenpos.products.tobe.domain.Product;
import kitchenpos.products.tobe.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MenuAntiCorruptionService {

    private final ProductRepository productRepository;

    public MenuAntiCorruptionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MenuProducts createMenuProducts(List<MenuCreateProductRequest> menuCreateProductRequests) {
        final List<Product> products = productRepository.findAllByIdIn(
                menuCreateProductRequests.stream()
                        .map(MenuCreateProductRequest::getProductId)
                        .collect(Collectors.toList())
        );
        if (products.size() != menuCreateProductRequests.size()) {
            throw new IllegalArgumentException();
        }

        var productMap = products.stream()
                .map(it -> new kitchenpos.menus.tobe.domain.Product(it.getId(), it.getPrice()))
                .collect(Collectors.toMap(kitchenpos.menus.tobe.domain.Product::getProductId, Function.identity()));

        var menuProducts = menuCreateProductRequests.stream().map(it -> {
            var product = productMap.get(it.getProductId());
            return MenuProduct.of(product, it.getQuantity());
        }).collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }
}
