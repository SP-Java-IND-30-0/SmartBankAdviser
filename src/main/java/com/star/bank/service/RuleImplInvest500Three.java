package com.star.bank.service;

import org.springframework.stereotype.Component;

@Component
public class RuleImplInvest500Three implements Rule {
    @Override
    public String getSubQuery() {
        return "EXISTS (SELECT 1 FROM transactions t \n" +
                "        JOIN products p ON t.product_id = p.id \n" +
                "        WHERE p.type = 'SAVING' \n" +
                "        AND t.user_id = ? \n" +
                "        AND t.transaction_type = 'DEPOSIT' \n" +
                "        GROUP BY t.user_id \n" +
                "        HAVING SUM(t.amount) > 1000)";
    }
}
//Сумма пополнений продуктов с типом SAVING больше 1000 ₽