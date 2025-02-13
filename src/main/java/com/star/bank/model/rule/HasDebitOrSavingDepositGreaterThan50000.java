package com.star.bank.model.rule;

import org.springframework.stereotype.Component;

@Component
public class HasDebitOrSavingDepositGreaterThan50000 implements Rule {
    @Override
    public String getSubQuery() {
        return """
                SUM(CASE WHEN P.TYPE = 'SAVING' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END) > 50000
                OR
                SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END) > 50000
                """;
    }
}

/*Сумма пополнений по всем продуктам типа DEBIT больше
или равна 50 000 ₽ ИЛИ сумма пополнений по всем продуктам
типа SAVING больше или равна 50 000 ₽
 */