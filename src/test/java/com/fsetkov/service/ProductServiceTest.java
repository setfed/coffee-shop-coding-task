package com.fsetkov.service;

import com.fsetkov.models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Test
    void shouldMakeFirstProductByTypeFree() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final ExtraProduct foamedMilkProduct = new ExtraProduct("Foamed milk", 0.51);
        final ExtraProduct roastCoffee = new ExtraProduct("Roast coffee", 0.95);

        ProductService productService = new ProductService(List.of(extraMilkProduct, foamedMilkProduct, roastCoffee));
        List<Product> products = productService.getProducts();

        // when
        productService.makeFirstProductByTypeFree(products, product -> product.getProductType() == ProductType.EXTRA);

        // then
        assertEquals(0d, products.get(0).getPrice());
    }

    @Test
    void shouldGetSublistByProductTypePredicate() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final ExtraProduct foamedMilkProduct = new ExtraProduct("Foamed milk", 0.52);
        final ExtraProduct roastCoffee = new ExtraProduct("Roast coffee", 0.92);

        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        final CoffeeProduct mediumCoffee = new CoffeeProduct("Medium coffee", 3.05);
        final CoffeeProduct bigCoffee = new CoffeeProduct("Big coffee", 3.55);

        ProductService productService = new ProductService(List.of(
                extraMilkProduct,
                foamedMilkProduct,
                roastCoffee,
                smallCoffee,
                mediumCoffee,
                bigCoffee));

        // when
        List<Product> sublist = productService.getSublistByProductTypePredicate(
                productService.getProducts(),
                product -> product.getProductType() == ProductType.BEVERAGE);

        // then
        assertEquals(sublist.size(), 3);
        for (Product product : sublist) {
            assertEquals(ProductType.BEVERAGE, product.getProductType());
        }
    }

    @Test
    void shouldApplyFreeProducts() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new CoffeeProduct("Big Coffee", 5.0));
        products.add(new CoffeeProduct("Medium Coffee", 4.0));
        products.add(new BeverageProduct("Orange juice", 1.0));
        products.add(new ExtraProduct("Roast Cofee", 0.5));

        int freeBeveragesCount = 2;
        boolean isEligibleForFreeExtra = true;
        ProductService productService = new ProductService(List.of());

        // when
        productService.applyFreeProducts(
                products,
                freeBeveragesCount,
                isEligibleForFreeExtra);

        // then
        assertEquals(0, products.get(0).getPrice());
        assertEquals(0, products.get(1).getPrice());
        assertEquals(1, products.get(2).getPrice());
        assertEquals(0, products.get(3).getPrice());
    }

    @Test
    void shouldMakeFirstNProductsByTypeFree() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new CoffeeProduct("Big Coffee", 5.0));
        products.add(new CoffeeProduct("Medium Coffee", 4.0));
        products.add(new BeverageProduct("Orange juice", 1.0));
        products.add(new SnackProduct("KitKat", 0.5));

        int n = 2;
        ProductService productService = new ProductService(List.of());

        // when
        productService.makeFirstNProductsByTypeFree(
                products,
                p -> p.getProductType() == ProductType.BEVERAGE,
                n);

        // then
        assertEquals(0, products.get(0).getPrice());
        assertEquals(0, products.get(1).getPrice());
        assertEquals(1, products.get(2).getPrice());
        assertEquals(0.5, products.get(3).getPrice());
    }
}