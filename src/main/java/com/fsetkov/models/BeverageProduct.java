package com.fsetkov.models;

public class BeverageProduct extends Product {

    public BeverageProduct(String name, double price) {
        super(name, ProductType.BEVERAGE, price);
    }

    @Override
    public BeverageProduct copy() {
        return new BeverageProduct(this.getName(), this.getPrice());
    }
}
