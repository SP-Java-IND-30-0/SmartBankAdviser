package com.star.bank.model.rule;

import org.springframework.stereotype.Component;

@Component
public class HasNotCreditTypeProduct implements Rule {
    @Override
    public String getSubQuery() {
        return "COUNT(CASE WHEN P.TYPE = 'CREDIT' THEN 1 END) = 0";
    }
}
//Пользователь не использует продукты с типом CREDIT