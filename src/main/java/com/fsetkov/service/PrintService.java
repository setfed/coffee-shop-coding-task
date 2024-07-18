package com.fsetkov.service;

import com.fsetkov.exception.ExitException;
import com.fsetkov.models.CustomerStampCard;
import com.fsetkov.models.Product;
import com.fsetkov.util.Constants;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;

/**
 * Service class for printing messages.
 * This class provides methods to print various messages to the console.
 */
public class PrintService {

    private static final String HELLO_MESSAGE_AND_ASK_CUSTOMER_NAME_MESSAGE =
            "Hello at our coffee shop, what is your name?";
    private static final String HELLO_CUSTOMER_NAME_MESSAGE_TEMPLATE =
            format("Hello %%s%s", ls());
    private static final String WRITE_CHOICE_OR_END_TEMPLATE = format(
            "Write your choice number,%swhen you finish write '%%s'%s",
            ls(), ls());
    private static final String EXTRA_CHOICE_MESSAGE_TEMPLATE = format(
            "You can choose additional extra for your choice%s", ls());
    public static final String RECEIPT_TEMPLATE = String.join(System.lineSeparator(),
            Constants.DIVIDER_LINE,
            "Description of Goods:",
            "%1s",
            Constants.DIVIDER_LINE,
            "Total: %2$.2f CHF",
            Constants.DIVIDER_LINE,
            "");
    private static final String RECEIPT_ROW_TEMPLATE =
            format("%%2d. %%2$-30s | %%3$.2f %%4$3s%s", ls());

    private final ProductService productService;
    private final Scanner scanner;

    /**
     * Constructs a new PrintService with the specified ProductService and Scanner.
     *
     * @param productService the product service used for managing products
     * @param scanner        the scanner used for reading user input
     */
    public PrintService(ProductService productService, Scanner scanner) {
        this.productService = productService;
        this.scanner = scanner;
    }

    /**
     * Prints a welcome message and asks the customer for their name.
     */
    public void printHelloMessageAndAskCustomerNameMessage() {
        System.out.println(HELLO_MESSAGE_AND_ASK_CUSTOMER_NAME_MESSAGE);
    }

    /**
     * Prints a personalized welcome message for the customer.
     *
     * @param customerName the name of the customer
     */
    public void printHelloCustomerMessage(String customerName) {
        System.out.printf(HELLO_CUSTOMER_NAME_MESSAGE_TEMPLATE, customerName);
    }

    /**
     * Prints a message prompting the customer to write their choice or end the selection.
     */
    public void printWriteChoiceOrEnd() {
        System.out.printf(WRITE_CHOICE_OR_END_TEMPLATE,
                Constants.END_CUSTOMER_CHOICE_OPTION);
    }

    /**
     * Prints a list of possible products for the customer to choose from.
     *
     * @param products the collection of products to display
     */
    public void printPossibleProducts(Collection<Product> products) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 1;

        for (Product product : products) {
            stringBuilder.append(format(
                    "%2d. %-30s | %4.2f " + Constants.CURRENCY_NAME + System.lineSeparator(),
                    index++, product.getName(), product.getPrice()));
        }

        System.out.println(stringBuilder);
    }

    /**
     * Prints a receipt for the customer's purchase, including any applicable discounts.
     *
     * @param buyingList             the list of products the customer is buying
     * @param stampCard              the customer's stamp card
     * @param isEligibleForFreeExtra whether the customer is eligible for a free extra product
     */
    public void printReceipt(List<Product> buyingList,
                             CustomerStampCard stampCard,
                             boolean isEligibleForFreeExtra) {
        if (buyingList.isEmpty()) {
            return;
        }

        StringBuilder receiptBuilder = new StringBuilder();
        int index = 1;

        buyingList.sort(Product.getProductTypeAndPriceComparator());
        productService.applyFreeProducts(
                buyingList,
                stampCard.getNumberOfFreeBeverages(),
                isEligibleForFreeExtra);

        for (Product product : buyingList) {
            if (!Constants.NO_EXTRA_NAME.equals(product.getName())) {
                receiptBuilder.append(format(
                        RECEIPT_ROW_TEMPLATE,
                        index++,
                        product.getName(),
                        product.getPrice(),
                        Constants.CURRENCY_NAME));
            }
        }

        double finalPrice = buyingList.stream().mapToDouble(Product::getPrice).sum();
        System.out.printf(RECEIPT_TEMPLATE, receiptBuilder, finalPrice);
    }

    /**
     * Prompts the user to confirm if they want to end the program.
     * If the user types 'exit', the program will terminate.
     */
    public void affirmateToEndProgram() {
        System.out.printf("Do you want to end program? If yes print '%s', " +
                          "if no press ENTER" + ls(), Constants.EXIT_PROGRAM_CHOICE);
        String choice = scanner.nextLine();
        if (choice.equals(Constants.EXIT_PROGRAM_CHOICE)) {
            throw new ExitException("End program");
        }
    }

    /**
     * Prints a message prompting the customer to choose an additional extra product.
     */
    public void printExtraChoiceMessage() {
        System.out.printf(EXTRA_CHOICE_MESSAGE_TEMPLATE);
    }

    /**
     * Retrieves a product from the user's input.
     * <p>
     * This method prompts the user to enter a product choice and attempts to retrieve the corresponding product
     * from the provided list of products. If the input is invalid, the user is prompted to enter a valid
     * product index or to write 'end' to terminate the selection process.
     *
     * @param <T>           the type of product, which must extend the {@link Product} class
     * @param productChoice the initial product choice made by the user
     * @param products      the list of available products
     * @param scanner       the {@code Scanner} object used to read user input
     * @return the chosen product if a valid choice is made; {@code null} if the user decides to end the selection process
     */
    public <T extends Product> T getProductFromUserInput(String productChoice,
                                                         List<T> products,
                                                         Scanner scanner) {
        T choosedProduct = null;
        while (choosedProduct == null) {
            try {
                int productIndex = getCorrectIndex(productChoice);
                choosedProduct = (T) products.get(productIndex).copy();
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a valid product index or write 'end'");
                productChoice = scanner.nextLine();
                if (isCustomerEndHisChoice(productChoice)) {
                    break;
                }
            }
        }
        return choosedProduct;
    }

    /**
     * Converts the given choice to an index by parsing it as an integer and subtracting one.
     *
     * @param choice the choice to convert to an index
     * @return the index corresponding to the given choice
     * @throws NumberFormatException if the choice is not a valid integer
     */
    public int getCorrectIndex(String choice) {
        return Integer.parseInt(choice) - 1;
    }

    /**
     * Prints and returns a list of products that match a given predicate.
     *
     * @param productService the ProductService to use for getting the sublist
     * @param products       the list of products to filter
     * @param predicate      the predicate to use for filtering the products
     * @return a list of products that match the given predicate
     */
    public List<Product> printAndGetProductsListByPredicate(
            ProductService productService,
            List<Product> products,
            Predicate<Product> predicate) {

        List<Product> extraProducts = productService.getSublistByProductTypePredicate(
                products,
                predicate);
        printPossibleProducts(extraProducts);

        return extraProducts;
    }

    /**
     * Prints a message to the console indicating that the user has chosen a product with the given name.
     *
     * @param name the name of the chosen product
     */
    public void printProductNameForCustomer(String name) {
        System.out.printf("You chose %s" + ls(), name);
    }

    /**
     * Checks if the customer's product choice indicates the end of their selection process.
     *
     * @param productChoice the product choice made by the customer
     * @return {@code true} if the product choice is equal to the constant
     * indicating the end of the customer's choice process; {@code false} otherwise
     */
    public boolean isCustomerEndHisChoice(String productChoice) {
        return productChoice.equals(Constants.END_CUSTOMER_CHOICE_OPTION);
    }

    private static String ls() {
        return lineSeparator();
    }
}
