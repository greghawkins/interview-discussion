package com.generic.retailer.domain;

import com.generic.retailer.exceptions.ProductNotFoundException;

public class ProductFactory {
    public static Product getProduct(String productType) throws ProductNotFoundException {
        if (productType.equalsIgnoreCase(ProductType.BOOK.name())) {
            return new Book();
        }
        else if (productType.equalsIgnoreCase(ProductType.CD.name())) {
            return new CD();
        }
        else if (productType.equalsIgnoreCase(ProductType.DVD.name())) {
            return new DVD();
        }
        throw new ProductNotFoundException();
    }
}
