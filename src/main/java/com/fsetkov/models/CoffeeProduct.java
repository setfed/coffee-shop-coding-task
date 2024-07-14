package com.fsetkov.models;

public class CoffeeProduct extends BeverageProduct {

    public CoffeeProduct(String name, double price) {
        super(name, price);
    }

    @Override
    public CoffeeProduct copy() {
        return new CoffeeProduct(this.getName(), this.getPrice());
    }

}
