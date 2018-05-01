package com.generic.retailer.domain;

public class DVD extends Product {
    private final double price = 15;
    private final ProductType productType = ProductType.DVD;

    public double getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }
}
