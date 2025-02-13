package com.star.bank.model.rule;

import org.springframework.stereotype.Component;

@Component
public class HasDebitTypeProduct implements Rule {
    @Override
    public String getSubQuery() {
        return "COUNT(CASE WHEN P.TYPE = 'DEBIT' THEN 1 END) > 0";
    }
}
//Пользователь использует как минимум один продукт с типом DEBIT