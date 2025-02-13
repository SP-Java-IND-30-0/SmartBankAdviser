package com.star.bank.service;

import org.springframework.stereotype.Component;

@Component
public class RuleImplTopSavingTwo implements Rule {
    @Override
    public String getSubQuery() {
        return "(\n" +
                "    SELECT SUM(t.amount) \n" +
                "    FROM transactions t \n" +
                "    JOIN products p ON t.product_id = p.id \n" +
                "    WHERE t.user_id = ? AND t.transaction_type = 'DEPOSIT' \n" +
                "    AND (p.type = 'DEBIT' OR p.type = 'SAVING')\n" +
                ") >= 50000";
    }
}

/*Сумма пополнений по всем продуктам типа DEBIT больше
или равна 50 000 ₽ ИЛИ сумма пополнений по всем продуктам
типа SAVING больше или равна 50 000 ₽
 */