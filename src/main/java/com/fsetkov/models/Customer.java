package com.fsetkov.models;

public class Customer {
    private final String username;
    private final CustomerStampCard customerStampCard;

    public Customer(String username) {
        this.username = username;
        this.customerStampCard = new CustomerStampCard();
    }

    public String getUsername() {
        return username;
    }

    public CustomerStampCard getCustomerStampCard() {
        return customerStampCard;
    }
}
