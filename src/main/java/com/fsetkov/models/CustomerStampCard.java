package com.fsetkov.models;

public class CustomerStampCard {
    private Integer numberOfBeveragesBought;

    public Integer getNumberOfBeveragesBought() {
        return numberOfBeveragesBought;
    }

    public CustomerStampCard() {
        numberOfBeveragesBought = 0;
    }

    public void increaseBeverageCount() {
        numberOfBeveragesBought += 1;
    }

    public int getNumberOfFreeBeverages() {
        int freeBeverages = numberOfBeveragesBought / 5;
        numberOfBeveragesBought %= 5; // Update the count to reflect the used free beverages
        return freeBeverages;
    }


    public void setNumberOfBeveragesBought(Integer numberOfBeveragesBought) {
        this.numberOfBeveragesBought = numberOfBeveragesBought;
    }
}
