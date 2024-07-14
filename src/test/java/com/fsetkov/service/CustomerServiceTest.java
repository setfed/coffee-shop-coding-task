package com.fsetkov.service;

import com.fsetkov.models.Customer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    void shouldGetExistedCustomerByUsername() {
        // given
        String testCustomerName = "testCustomer";
        Customer testCustomer = new Customer(testCustomerName);
        testCustomer.getCustomerStampCard().setNumberOfBeveragesBought(2);
        HashMap<String, Customer> customerRepo = new HashMap<>();
        customerRepo.put(testCustomerName, testCustomer);
        CustomerService customerService = new CustomerService(customerRepo);

        // when
        Customer result = customerService.getCustomerByUsername(testCustomerName);

        // then
        assertEquals(testCustomerName, result.getUsername());
        assertEquals(result.getCustomerStampCard().getNumberOfBeveragesBought(), 2);
    }

    @Test
    void shouldCreateCustomerIfNotExist() {
        // given
        String testExpectedCustomerName = "testCustomer";
        HashMap<String, Customer> customerRepo = new HashMap<>();
        CustomerService customerService = new CustomerService(customerRepo);

        // when
        Customer result = customerService.getCustomerByUsername(testExpectedCustomerName);

        // then
        assertEquals(testExpectedCustomerName, result.getUsername());
        assertEquals(result.getCustomerStampCard().getNumberOfBeveragesBought(), 0);
    }
}