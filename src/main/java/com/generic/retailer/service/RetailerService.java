package com.generic.retailer.service;

import com.generic.retailer.domain.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public final class RetailerService {

    private final Trolley trolley = new Trolley();

    private final int RECEIPT_WIDTH = 20;

    public void addProductToTrolley(Product product) {
        trolley.addProduct(product);
    }

    private double calculateTotalCostForAllProducts() {
        return trolley.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    private double calculateTotalCostForAllApartFromDvds() {
       return trolley.getProducts().stream()
                .filter(p -> !p.getProductType().equals(ProductType.DVD))
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public String getReceipt(LocalDate dateOfPurchase) {
        Map<ProductType, List<Product>> productsByType = trolley.getProducts().stream()
                .collect(groupingBy(Product::getProductType));
        StringBuilder stringBuilder = new StringBuilder();
        printReceipt(stringBuilder, productsByType, dateOfPurchase);
        return stringBuilder.toString();
    }

    private void printReceipt(StringBuilder stringBuilder, Map<ProductType, List<Product>> productsByType, LocalDate dateOfPurchase) {
        stringBuilder.append("===== RECEIPT ======");
        stringBuilder.append(System.lineSeparator());
        Arrays.stream(ProductType.values()).forEach(productType -> {
            if (productsByType.get(productType) != null) {
                String productTotal = "£" +
                        String.format("%.2f", productsByType.get(productType).stream()
                                .mapToDouble(Product::getPrice)
                                .sum());
                int noOfProducts = productsByType.get(productType).size();
                if (!productTotal.equals("£0.00")) {
                    stringBuilder.append(generateProductLineOfReceipt(productType, noOfProducts, productTotal));
                }
            }
        });
        DealsResult dealsResult = printDeals(stringBuilder, productsByType, dateOfPurchase);
        stringBuilder.append("====================");
        stringBuilder.append(System.lineSeparator());
        printTotalCost(stringBuilder, dealsResult);
    }

    private String generateProductLineOfReceipt(ProductType productType, int noOfProducts, String productTotal) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(productType.name());
        if (noOfProducts > 1) {
            stringBuilder.append(" (x");
            stringBuilder.append(noOfProducts);
            stringBuilder.append(")");
        }
        int tabWidth = RECEIPT_WIDTH - stringBuilder.toString().length();
        stringBuilder.append(String.format("%" + tabWidth + "s", productTotal));
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }

    private DealsResult printDeals(StringBuilder stringBuilder, Map<ProductType, List<Product>> productsByType, LocalDate dateOfPurchase) {
        DealsResult dealsResult = populateDealRelatedInformation(productsByType, dateOfPurchase);
        print2For1DvdDeal(stringBuilder, dealsResult);
        printThursdayDeal(stringBuilder, dealsResult);
        return dealsResult;
    }

    private void print2For1DvdDeal(StringBuilder stringBuilder, DealsResult dealsResult) {
        if (dealsResult.getNumberOfTwoFor1DvdDeals() > 0) {
            String twoForOneText = "2 FOR 1";
            if (dealsResult.getNumberOfTwoFor1DvdDeals() > 1) {
                twoForOneText += " (x" + dealsResult.getNumberOfTwoFor1DvdDeals() + ")";
            }
            stringBuilder.append(twoForOneText);
            int tabWidth = RECEIPT_WIDTH - twoForOneText.length();
            stringBuilder.append(String.format("%" + tabWidth + "s", "-£" + String.format("%.2f", dealsResult.getDvdReduction())));
            stringBuilder.append(System.lineSeparator());
        }
    }
    private void printThursdayDeal(StringBuilder stringBuilder, DealsResult dealsResult) {
        if (dealsResult.isDateOfPurchaseAThursday()) {
            double totalCostForAllWithoutDvds = calculateTotalCostForAllApartFromDvds();
            double thursdayReduction = dealsResult.getThursdayReduction(totalCostForAllWithoutDvds);
            stringBuilder.append("THURS");
            stringBuilder.append(String.format("%15s", "-£" + String.format("%.2f", thursdayReduction)));
            stringBuilder.append(System.lineSeparator());
        }
    }

    private DealsResult populateDealRelatedInformation(Map<ProductType, List<Product>> productsByType, LocalDate dateOfPurchase) {
        if (productsByType.get(ProductType.DVD) != null && productsByType.get(ProductType.DVD).size() > 0) {
            return new DealsResult(productsByType.get(ProductType.DVD).size(), dateOfPurchase);
        }
        return new DealsResult(dateOfPurchase);
    }

    private void printTotalCost(StringBuilder stringBuilder, DealsResult dealsResult) {
        stringBuilder.append("TOTAL");
        double totalCostWithReduction = getGrandTotalWithReductions(dealsResult);
        stringBuilder.append(String.format("%15s", "£" +  String.format("%.2f", totalCostWithReduction)));
    }

    private double getGrandTotalWithReductions(DealsResult dealsResult) {
        double totalCost = calculateTotalCostForAllProducts();
        double totalCostWithReduction = totalCost - dealsResult.getThursdayReduction(calculateTotalCostForAllApartFromDvds()) - dealsResult.getDvdReduction();
        return totalCostWithReduction;
    }

    public double getGrandTotalWithReductions(LocalDate dateOfPurchase) {
        Map<ProductType, List<Product>> productsByType = trolley.getProducts().stream()
                .collect(groupingBy(Product::getProductType));
        DealsResult dealsResult = populateDealRelatedInformation(productsByType, dateOfPurchase);
        return getGrandTotalWithReductions(dealsResult);
    }
}
