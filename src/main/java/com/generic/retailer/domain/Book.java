package com.generic.retailer.domain;

public final class Book extends Product {
    private final int price = 5;
    private final ProductType productType = ProductType.BOOK;

    public double getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }
}
