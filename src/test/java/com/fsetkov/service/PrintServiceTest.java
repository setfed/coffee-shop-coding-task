package com.fsetkov.service;

import com.fsetkov.models.CoffeeProduct;
import com.fsetkov.models.CustomerStampCard;
import com.fsetkov.models.ExtraProduct;
import com.fsetkov.models.Product;
import com.fsetkov.models.ProductType;
import com.fsetkov.models.SnackProduct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PrintServiceTest {

    private PrintService printService;
    private Scanner scanner;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        scanner = new Scanner(System.in);
        printService = new PrintService(new ProductService(List.of()), scanner);
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        scanner.close();
    }

    @ParameterizedTest
    @CsvSource({"test, Hello test", "otherString, Hello otherString"})
    void shouldPrintHelloCustomerMessage(String customerName,
                                         String expectedOutput) {
        // when
        printService.printHelloCustomerMessage(customerName);

        // then
        assertEquals(expectedOutput + System.lineSeparator(), out.toString());
    }

    @Test
    void shouldPrintPossibleProducts() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final ExtraProduct foamedMilkProduct = new ExtraProduct("Foamed milk", 0.51);
        final ExtraProduct roastCoffee = new ExtraProduct("Roast coffee", 0.95);
        List<Product> products = List.of(
                extraMilkProduct,
                foamedMilkProduct,
                roastCoffee);
        printService = new PrintService(new ProductService(products), scanner);

        // when
        printService.printPossibleProducts(products);

        // then
        String actualOutput = out.toString();
        String expectedOutput = " 1. Extra milk                     | 0.32 CHF"
                                + System.lineSeparator() +
                                " 2. Foamed milk                    | 0.51 CHF"
                                + System.lineSeparator() +
                                " 3. Roast coffee                   | 0.95 CHF"
                                + System.lineSeparator() + System.lineSeparator();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void shouldPrintReceipt_noBonuses() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        final SnackProduct baconRoll = new SnackProduct("Bacon roll", 4.53);
        List<Product> products = new ArrayList<>();
        products.add(extraMilkProduct);
        products.add(smallCoffee);
        products.add(baconRoll);

        // when
        printService.printReceipt(products, new CustomerStampCard(), false);

        // then
        String actualOutput = out.toString();
        String expectedOutput = "-----------------------------------------" + System.lineSeparator() +
                                "Description of Goods:" + System.lineSeparator() +
                                " 1. Small coffee                   | 2.55 CHF" + System.lineSeparator() +
                                " 2. Bacon roll                     | 4.53 CHF" + System.lineSeparator() +
                                " 3. Extra milk                     | 0.32 CHF" + System.lineSeparator() +
                                System.lineSeparator() +
                                "-----------------------------------------" + System.lineSeparator() +
                                "Total: 7.40 CHF" + System.lineSeparator() +
                                "-----------------------------------------" + System.lineSeparator();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void shouldPrintReceipt_freeOneBeverageBonus() {
        // given
        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        List<Product> products = new ArrayList<>();
        products.add(smallCoffee);

        // when
        CustomerStampCard stampCard = new CustomerStampCard();
        stampCard.setNumberOfBeveragesBought(5);
        printService.printReceipt(products, stampCard, false);

        // then
        String actualOutput = out.toString();
        String expectedOutput =
                String.join(System.lineSeparator(),
                        "-----------------------------------------",
                        "Description of Goods:",
                        " 1. Small coffee                   | 0.00 CHF",
                        "",
                        "-----------------------------------------",
                        "Total: 0.00 CHF",
                        "-----------------------------------------",
                        "");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void shouldPrintReceipt_freeTwoBeverageBonus() {
        // given
        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        final CoffeeProduct bigCoffee = new CoffeeProduct("Big coffee", 4.55);
        List<Product> products = new ArrayList<>();
        products.add(smallCoffee);
        products.add(bigCoffee);

        // when
        CustomerStampCard stampCard = new CustomerStampCard();
        stampCard.setNumberOfBeveragesBought(10);
        printService.printReceipt(products, stampCard, false);

        // then
        String actualOutput = out.toString();
        String expectedOutput =
                String.join(System.lineSeparator(),
                        "-----------------------------------------",
                        "Description of Goods:",
                        " 1. Big coffee                     | 0.00 CHF",
                        " 2. Small coffee                   | 0.00 CHF",
                        "",
                        "-----------------------------------------",
                        "Total: 0.00 CHF",
                        "-----------------------------------------",
                        "");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void shouldPrintReceipt_freeExtraBonus() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        final SnackProduct baconRoll = new SnackProduct("Bacon roll", 4.53);
        List<Product> products = new ArrayList<>();
        products.add(extraMilkProduct);
        products.add(smallCoffee);
        products.add(baconRoll);

        // when
        printService.printReceipt(products, new CustomerStampCard(), true);

        // then
        String actualOutput = out.toString();
        String expectedOutput = String.join(System.lineSeparator(),
                "-----------------------------------------",
                "Description of Goods:",
                " 1. Small coffee                   | 2.55 CHF",
                " 2. Bacon roll                     | 4.53 CHF",
                " 3. Extra milk                     | 0.00 CHF",
                "",
                "-----------------------------------------",
                "Total: 7.08 CHF",
                "-----------------------------------------",
                "");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void shouldPrintReceipt_freeExtraBonusFreeBeverage() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final CoffeeProduct smallCoffee = new CoffeeProduct("Small coffee", 2.55);
        final SnackProduct baconRoll = new SnackProduct("Bacon roll", 4.53);
        List<Product> products = new ArrayList<>();
        products.add(extraMilkProduct);
        products.add(smallCoffee);
        products.add(baconRoll);

        // when
        CustomerStampCard stampCard = new CustomerStampCard();
        stampCard.setNumberOfBeveragesBought(5);
        printService.printReceipt(products, stampCard, true);

        // then
        String actualOutput = out.toString();
        String expectedOutput =
                String.join(System.lineSeparator(),
                        "-----------------------------------------",
                        "Description of Goods:",
                        " 1. Small coffee                   | 0.00 CHF",
                        " 2. Bacon roll                     | 4.53 CHF",
                        " 3. Extra milk                     | 0.00 CHF",
                        "",
                        "-----------------------------------------",
                        "Total: 4.53 CHF",
                        "-----------------------------------------",
                        "");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void shouldGetProductFromUserInput() {
        // given
        List<Product> products = Arrays.asList(
                new CoffeeProduct("Coffee1", 1.0),
                new CoffeeProduct("Coffee2", 2.0),
                new CoffeeProduct("Coffee3", 3.0)
        );
        String input = "1" + System.lineSeparator(); // Simulate user input of "1"
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // when
        Product product = printService.getProductFromUserInput("1", products, scanner);

        // then
        assertEquals("Coffee1", product.getName());
        assertEquals(1.0, product.getPrice());
    }

    @Test
    void testGetProductFromUserInputInvalidChoice() {
        // given
        List<Product> products = Arrays.asList(
                new CoffeeProduct("Coffee1", 1.0),
                new CoffeeProduct("Coffee2", 2.0),
                new CoffeeProduct("Coffee3", 3.0)
        );
        String input = "4" + System.lineSeparator() +
                       "end" + System.lineSeparator(); // Simulate user
        // input of "4" and then "end"
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        // when
        printService.getProductFromUserInput("4", products, scanner);

        // then
        assertTrue(out.toString()
                .contains("Invalid choice. Please enter a valid product index or write 'end'"));
    }

    @Test
    void testGetProductFromUserInputInvalidThenValidChoice() {
        // given
        List<Product> products = Arrays.asList(
                new CoffeeProduct("Coffee1", 1.0),
                new CoffeeProduct("Coffee2", 2.0),
                new CoffeeProduct("Coffee3", 3.0)
        );
        String input = "4" + System.lineSeparator() +
                       "1" + System.lineSeparator(); // Simulate user input of "4" (invalid) and then "1"
        // (valid)
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        // when
        Product product = printService.getProductFromUserInput("4", products, scanner);

        // then
        assertTrue(out.toString().contains("Invalid choice. Please enter a valid product index or write 'end'"));
        assertEquals("Coffee1", product.getName());
        assertEquals(1.0, product.getPrice());
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,1", "3,2"})
    void testGetCorrectIndex(String input, int output) {
        // when
        int index = printService.getCorrectIndex(input);

        // then
        assertEquals(output, index);
    }

    @Test
    void shouldPrintAndGetProductsListByPredicate() {
        // given
        final ExtraProduct extraMilkProduct = new ExtraProduct("Extra milk", 0.32);
        final CoffeeProduct mediumCoffee = new CoffeeProduct("Medium coffee", 3.05);
        List<Product> products = List.of(
                extraMilkProduct,
                mediumCoffee);
        ProductService productService = new ProductService(products);
        printService = new PrintService(productService, scanner);

        // when
        printService.printAndGetProductsListByPredicate(
                productService,
                products,
                product -> product.getProductType() == ProductType.EXTRA);

        // then
        String actualOutput = out.toString();
        String expectedOutput = " 1. Extra milk                     | 0.32 CHF"
                                + System.lineSeparator() + System.lineSeparator();
        assertEquals(expectedOutput, actualOutput);
    }
}