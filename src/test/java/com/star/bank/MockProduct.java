package com.star.bank;

import com.star.bank.model.product.Product;


public class MockProduct implements Product {
    private final String id;
    private final String name;
    private final String text;
    private String query;

    public MockProduct(String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public MockProduct(String id, String name, String text, String query) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.query = query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }
}
