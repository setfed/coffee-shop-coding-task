package com.fsetkov.models;

public class ExtraProduct extends Product {
    public ExtraProduct(String name, double price) {
        super(name, ProductType.EXTRA, price);
    }

    @Override
    public Product copy() {
        return new ExtraProduct(getName(), getPrice());
    }
}
