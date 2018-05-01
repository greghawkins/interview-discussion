package com.generic.retailer.domain;

public final class CD extends Product {
    private final int price = 10;
    private final ProductType productType = ProductType.CD;

    public double getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }
}

