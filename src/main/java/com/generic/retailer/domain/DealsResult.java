package com.generic.retailer.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DealsResult {
    private int numberOfDvds;
    private LocalDate dateOfPurchase;

    private final double THURSDAY_DISCOUNT_PERCENTAGE = 20.00;

    public DealsResult(int numberOfDvds, LocalDate dateOfPurchase) {
        this.numberOfDvds = numberOfDvds;
        this.dateOfPurchase = dateOfPurchase;
    }

    public DealsResult(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public int getNumberOfTwoFor1DvdDeals() {
        return numberOfDvds / 2;
    }

    public double getDvdReduction() {
        return new DVD().getPrice() * getNumberOfTwoFor1DvdDeals();
    }

    public double getThursdayReduction(double totalCostForAllWithoutDvds) {
        if (isDateOfPurchaseAThursday()) {
            double totalWithoutDvdsReduction = (totalCostForAllWithoutDvds / 100) * THURSDAY_DISCOUNT_PERCENTAGE;
            // as we offer 2-for-1 on DVDs, we will only offer 20% reduction on 1 dvd max (i.e if it is not part of a pair deal)
            // e.g. if they buy 5 DVDs, 4 of them will go into the 2-for-1 deal, and the 5th DVD will get a 20% reduction on it
            double singleDvdReduction = 0.00;
            if (numberOfDvds % 2 !=0) {
                singleDvdReduction = (new DVD().getPrice() / 100) * THURSDAY_DISCOUNT_PERCENTAGE;
            }
            return totalWithoutDvdsReduction + singleDvdReduction;
        }
        return 0;
    }

    public boolean isDateOfPurchaseAThursday() {
        return dateOfPurchase.getDayOfWeek().equals(DayOfWeek.THURSDAY);
    }


}
