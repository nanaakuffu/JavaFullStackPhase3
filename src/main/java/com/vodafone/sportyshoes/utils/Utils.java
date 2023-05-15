package com.vodafone.sportyshoes.utils;

import com.vodafone.sportyshoes.entities.CartProduct;
import com.vodafone.sportyshoes.entities.Purchase;
import com.vodafone.sportyshoes.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Slf4j
public class Utils {

    public static String getCartCount(User user) {
        log.info("User {}:\b cart: {}", user, user.getCart());
        return ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getCart())
                ? "0"
                : String.valueOf(user.getCart().stream().map(CartProduct::getQuantity).reduce(Integer::sum).orElse(0));
    }

    public static String getProducts(Purchase purchase) {
        return String.format("<pre>%s</pre>", purchase.getProducts()
                .stream()
                .map(p -> String.format("%s\t%s\t%s", p.getProduct().getName(), p.getQuantity(), p.getProduct().getCategory().getName()))
                .collect(Collectors.joining("\n")));
    }

    public static Double getTotalCost(List<CartProduct> cartProducts) {
        return cartProducts.stream().map(p -> p.getQuantity()*p.getProduct().getPrice()).reduce(Long::sum).orElse(0L)/100.0;
    }
}
