package com.star.bank.service;

import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class TopSavingProduct implements Product {
    private final String name = "Top Saving";
    private final String id = "59efc529-2fff-41af-baff-90ccd7402925";
    private final Set<Rule> rules;
    public TopSavingProduct(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String getQuery() {
        return "SELECT SUM(amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ? " +
                "HAVING SUM(amount) >= 50000";
    }

    private final String text = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n" +
            "\n" +
            "Преимущества «Копилки»:\n" +
            "\n" +
            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n" +
            "\n" +
            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n" +
            "\n" +
            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n" +
            "\n" +
            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

}
