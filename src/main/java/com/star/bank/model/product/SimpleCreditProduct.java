package com.star.bank.model.product;

import com.star.bank.model.rule.Rule;
import com.star.bank.utils.Literals;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;


public class SimpleCreditProduct implements Product {
    private final String name = "Простой кредит";
    private final String id = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private final Set<Rule> rules = new HashSet<>();

    public SimpleCreditProduct(@Qualifier("hasNotCreditTypeProduct") Rule rule1,
                               @Qualifier("hasDebitDepositGreaterThanWithdraw") Rule rule2,
                               @Qualifier("hasDebitWithdrawGreater100000") Rule rule3) {
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
    }


    @Override
    public String getQuery() {
        StringJoiner joiner = new StringJoiner(" AND ", Literals.QUERY_PREFIX, Literals.QUERY_SUFFIX);
        rules.forEach(rule -> joiner.add(rule.getSubQuery()));
        return joiner.toString();
    }


    private final String text = """
            Откройте мир выгодных кредитов с нами!
            Ищете способ быстро и без лишних хлопот получить нужную сумму?
            Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки,
            гибкие условия и индивидуальный подход к каждому клиенту.
            Почему выбирают нас:
            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает
            всего несколько часов.
            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.
            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости,
            автомобиля, образование, лечение и многое другое.
            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!
            """;

}
