package com.generic.retailer.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("The product entered is was not found");
    }
}
