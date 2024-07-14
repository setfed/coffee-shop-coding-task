package com.fsetkov;

import com.fsetkov.models.*;
import com.fsetkov.service.CustomerService;
import com.fsetkov.service.PrintService;
import com.fsetkov.service.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class EntryPoint {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            CustomerService customerService = new CustomerService(new HashMap<>());
            ProductService productService = new ProductService();
            List<Product> products = productService.getProducts();
            List<Product> nonExtraProducts = productService.getSublistByProductTypePredicate(
                    products,
                    p -> p.getProductType() != ProductType.EXTRA);

            PrintService printService = new PrintService(productService, scanner);

            while (true) {
                printService.printHelloMessageAndAskCustomerNameMessage();
                String customerName = scanner.nextLine();
                Customer customer = customerService.getCustomerByUsername(customerName);
                List<Product> customerOrderList = new ArrayList<>();

                printService.printHelloCustomerMessage(customerName);

                boolean isOrderedBeverage = false;
                boolean isOrderedSnack = false;
                printService.printWriteChoiceOrEnd();
                printService.printPossibleProducts(nonExtraProducts);
                String productChoice = scanner.nextLine();

                while (!printService.isCustomerEndHisChoice(productChoice)) {
                    Product choosedProduct = printService.getProductFromUserInput(
                            productChoice,
                            nonExtraProducts,
                            scanner);
                    if (choosedProduct == null) {
                        printService.printWriteChoiceOrEnd();
                        printService.printPossibleProducts(nonExtraProducts);
                        productChoice = scanner.nextLine();
                        continue;
                    }
                    printService.printProductNameForCustomer(choosedProduct.getName());

                    if (choosedProduct.getProductType() == ProductType.BEVERAGE) {
                        customer.getCustomerStampCard().increaseBeverageCount();
                        isOrderedBeverage = true;
                    }

                    if (choosedProduct.getProductType() == ProductType.SNACK) {
                        isOrderedSnack = true;
                    }

                    customerOrderList.add(choosedProduct);
                    if (choosedProduct.getClass() == CoffeeProduct.class) {
                        printService.printExtraChoiceMessage();
                        List<Product> extraProducts = printService.printAndGetProductsListByPredicate(
                                productService,
                                products,
                                p -> p.getProductType() == ProductType.EXTRA);
                        String extraChoice = scanner.nextLine();
                        Product chosenExtraProduct = printService.getProductFromUserInput(
                                extraChoice,
                                extraProducts,
                                scanner);
                        printService.printProductNameForCustomer(chosenExtraProduct.getName());
                        customerOrderList.add(chosenExtraProduct);
                    }
                    printService.printWriteChoiceOrEnd();
                    printService.printPossibleProducts(nonExtraProducts);
                    productChoice = scanner.nextLine();
                }

                boolean isEligibleForFreeExtra = isOrderedBeverage && isOrderedSnack;
                printService.printReceipt(
                        customerOrderList,
                        customer.getCustomerStampCard(),
                        isEligibleForFreeExtra);
                printService.affirmateToEndProgram();
            }
        }
    }
}