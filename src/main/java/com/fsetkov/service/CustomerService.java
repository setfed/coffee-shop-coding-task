package com.fsetkov.service;

import com.fsetkov.models.Customer;

import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing customers.
 * This class provides methods to retrieve and add customers by their username.
 */
public record CustomerService(Map<String, Customer> customers) {

    /**
     * Retrieves a customer by their username.
     * If the customer does not exist, a new customer is created and added to the map.
     *
     * @param username the username of the customer to retrieve
     * @return the customer associated with the given username
     */
    public Customer getCustomerByUsername(String username) {
        return Optional.ofNullable(customers.get(username))
                .orElse(addCustomer(username));
    }

    /**
     * Adds a new customer with the given username to the map.
     * This method is private and is used internally by {@link #getCustomerByUsername(String)}.
     *
     * @param username the username of the new customer
     * @return the newly created customer
     */
    private Customer addCustomer(String username) {
        Customer customer = new Customer(username);
        customers.put(username, customer);

        return customer;
    }
}
