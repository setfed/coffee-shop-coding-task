package com.fsetkov.models;

public class SnackProduct extends Product {

    public SnackProduct(String name, double price) {
        super(name, ProductType.SNACK, price);
    }

    @Override
    public SnackProduct copy() {
        return new SnackProduct(getName(), getPrice());
    }
}
