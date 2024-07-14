package com.fsetkov.models;

import java.util.Comparator;

public abstract class Product {
    private final String name;
    private final ProductType productType;
    private Double price;

    public void makeFree() {
        this.price = 0d;
    }

    public Product(String name, ProductType productType, Double price) {
        this.name = name;
        this.productType = productType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Double getPrice() {
        return price;
    }

    public abstract Product copy();

    public static Comparator<Product> getProductTypeAndPriceComparator() {
        return Comparator
                .comparingInt((Product p) -> p.getProductType().ordinal())
                .thenComparing(Comparator.comparingDouble(Product::getPrice).reversed());
    }
}
