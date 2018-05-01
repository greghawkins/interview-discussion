package com.generic.retailer.domain;

public enum ProductType {
    DVD("14") ,CD("15"), BOOK("13");

    private String receiptAlignment;

    ProductType(String receiptAlignment) {
        this.receiptAlignment = receiptAlignment;
    }

    public String getReceiptAlignment() {
        return receiptAlignment;
    }
}
