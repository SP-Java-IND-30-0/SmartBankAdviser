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
public class Invest500Product implements Product {
    @Getter
    private final String name = "Invest 500";
    @Getter
    private final String id = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private final Set<Rule> rules = new HashSet<>();

    public Invest500Product(@Qualifier("hasDebitTypeProduct") Rule rule1,
                            @Qualifier("hasNotInvestTypeProduct") Rule rule2,
                            @Qualifier("hasSavingDepositGreaterThan1000") Rule rule3) {
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
            Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка!
            Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца
            года и получите выгоду в виде вычета на взнос в следующем налоговом периоде.
            Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными
            рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!
            """;
}
