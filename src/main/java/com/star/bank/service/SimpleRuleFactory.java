package com.star.bank.service;

import com.star.bank.model.enums.QueryType;
import com.star.bank.model.rule.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Фабрика для создания простых правил на основе типа запроса.
 * Предоставляет метод для получения аргументов простого правила по типу запроса и списку аргументов.
 */
@Component
public class SimpleRuleFactory {

    /**
     * Возвращает аргументы простого правила на основе типа запроса и списка аргументов.
     *
     * @param queryType Тип запроса.
     * @param arguments Список аргументов для правила.
     * @return Аргументы простого правила.
     */
    public RuleArguments getSimpleRule(QueryType queryType, List<String> arguments) {
        return switch (queryType) {
            case USER_OF -> new RuleUserOf(arguments);
            case ACTIVE_USER_OF -> new RuleActiveUserOf(arguments);
            case TRANSACTION_SUM_COMPARE -> new RuleCompareSum(arguments);
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> new RuleCompareOperationSum(arguments);
        };
    }
}
