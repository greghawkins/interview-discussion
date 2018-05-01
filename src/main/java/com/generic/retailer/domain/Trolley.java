package com.generic.retailer.domain;

import java.util.*;

public final class Trolley {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public  List<Product> getProducts() {
        return products;
    }
}
