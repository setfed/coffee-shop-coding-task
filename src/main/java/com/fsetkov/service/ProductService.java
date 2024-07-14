package com.fsetkov.service;

import com.fsetkov.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.fsetkov.util.Constants.NO_EXTRA_NAME;

/**
 * Service class for managing products in the coffee shop application.
 * This class provides methods to initialize and retrieve products.
 */
public class ProductService {

    private final List<Product> products;

    /**
     * Constructs a new ProductService and initializes the list of products.
     */
    public ProductService() {
        this.products = initProducts();
    }

    public ProductService(List<Product> products) {
        this.products = products;
    }

    /**
     * Retrieves the list of products.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Makes the first product in the buying list that matches the given predicate free.
     *
     * @param buyingList       the list of products the customer is buying
     * @param productPredicate the predicate to match the product
     */
    public void makeFirstProductByTypeFree(List<Product> buyingList,
                                           Predicate<Product> productPredicate) {
        buyingList.stream().filter(productPredicate).findFirst().ifPresent(Product::makeFree);
    }

    /**
     * Retrieves a sublist of products that match the given predicate.
     *
     * @param products         the list of products to filter
     * @param productPredicate the predicate to match the products
     * @return the sublist of products that match the predicate
     */
    public List<Product> getSublistByProductTypePredicate(List<Product> products,
                                                          Predicate<Product> productPredicate) {
        return products.stream().filter(productPredicate).toList();
    }

    /**
     * Applies free products to the buying list based on the given conditions.
     * <p>
     * This method applies free beverages and/or free extras to the buying list based on the provided parameters.
     * If the number of free beverages is greater than zero, it makes the first N beverages in the list free.
     * If the customer is eligible for a free extra, it makes the first extra in the list free.
     *
     * @param buyingList             the list of products that the customer is buying
     * @param freeBeveragesCount     the number of free beverages to apply
     * @param isEligibleForFreeExtra {@code true} if the customer is eligible for a free extra; {@code false} otherwise
     */
    public void applyFreeProducts(List<Product> buyingList,
                                  int freeBeveragesCount,
                                  boolean isEligibleForFreeExtra) {
        if (freeBeveragesCount > 0) {
            makeFirstNProductsByTypeFree(
                    buyingList,
                    p -> p.getProductType() == ProductType.BEVERAGE,
                    freeBeveragesCount);
        }

        if (isEligibleForFreeExtra) {
            makeFirstProductByTypeFree(
                    buyingList,
                    p -> p.getProductType() == ProductType.EXTRA);
        }
    }

    /**
     * Makes the first N products of a specified type free.
     * <p>
     * This method filters the provided list of products based on the given predicate and makes the first N products
     * that match the predicate free.
     *
     * @param products the list of products to be processed
     * @param filter   a predicate to filter the products by type
     * @param n        the number of products to make free
     */
    public void makeFirstNProductsByTypeFree(List<Product> products, Predicate<Product> filter, int n) {
        products.stream()
                .filter(filter)
                .limit(n)
                .forEach(Product::makeFree);
    }

    /**
     * Initializes the list of products.
     *
     * @return the list of initialized products
     */
    private static List<Product> initProducts() {
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final ExtraProduct foamedMilkProduct = new ExtraProduct("Foamed milk", 0.51);
        final ExtraProduct roastCoffee = new ExtraProduct("Special roast coffee", 0.95);
        final ExtraProduct noExtra = new ExtraProduct(NO_EXTRA_NAME, 0.00);

        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        final CoffeeProduct mediumCoffee = new CoffeeProduct("Medium coffee", 3.05);
        final CoffeeProduct bigCoffee = new CoffeeProduct("Big coffee", 3.55);

        final BeverageProduct orangeJuice = new BeverageProduct("Freshly squeezed orange juice", 3.95);

        final SnackProduct baconRoll = new SnackProduct("Bacon roll", 4.53);

        return new ArrayList<>(List.of(smallCoffee, mediumCoffee, bigCoffee,
                orangeJuice, baconRoll, extraMilkProduct, foamedMilkProduct, roastCoffee, noExtra));
    }
}
