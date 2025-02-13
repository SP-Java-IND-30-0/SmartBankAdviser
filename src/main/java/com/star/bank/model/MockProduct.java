package com.star.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockProduct implements Product {
    private String id;
    private String name;
    private String text;
}
