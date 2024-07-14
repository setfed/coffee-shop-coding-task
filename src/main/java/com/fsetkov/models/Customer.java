package com.fsetkov.models;

public class Customer {
    private String username;
    private CustomerStampCard customerStampCard;

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
