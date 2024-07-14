package com.fsetkov.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CustomerStampCardTest {

    @Test
    void shouldIncreaseBeverageCount() {
        CustomerStampCard customerStampCard = new CustomerStampCard();
        customerStampCard.increaseBeverageCount();
        customerStampCard.increaseBeverageCount();

        assertEquals(2, customerStampCard.getNumberOfBeveragesBought());
    }

    @ParameterizedTest
    @CsvSource({"10,2,0", "5,1,0", "6,1,1", "12,2,2"})
    void isEligibleForFreeBeverage(int boughtBeverages,
                                   int freeBeverages,
                                   int stampCardBeverages) {
        CustomerStampCard customerStampCard = new CustomerStampCard();
        customerStampCard.setNumberOfBeveragesBought(boughtBeverages);

        assertEquals(freeBeverages, customerStampCard.getNumberOfFreeBeverages());
        assertEquals(stampCardBeverages, customerStampCard.getNumberOfBeveragesBought());
    }
}