package com.star.bank.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.star.bank.model.rule.Rule;
import com.star.bank.utils.Literals;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Component
public class TopSavingProduct implements Product {
    @Getter
    private final String name = "Top Saving";
    @Getter
    private final String id = "59efc529-2fff-41af-baff-90ccd7402925";
    private final Set<Rule> rules = new HashSet<>();

    public TopSavingProduct(@Qualifier("hasDebitTypeProduct") Rule rule1,
                            @Qualifier("hasDebitDepositGreaterThanWithdraw") Rule rule2,
                            @Qualifier("hasDebitOrSavingDepositGreaterThan50000") Rule rule3) {
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
    }

    @JsonIgnore
    @Override
    public String getQuery() {
        StringJoiner joiner = new StringJoiner(" AND ", Literals.QUERY_PREFIX, Literals.QUERY_SUFFIX);
        rules.forEach(rule -> joiner.add(rule.getSubQuery()));
        return joiner.toString();
    }

    @Getter
    private final String text = """
            Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент,
            который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и
            потерянных квитанций — всё под контролем!
            Преимущества «Копилки»:
            Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически
            переводить определенную сумму на ваш счет.
            Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и
            корректируйте стратегию при необходимости.
            Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только
            через мобильное приложение или интернет-банкинг.
            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!
            """;

}
