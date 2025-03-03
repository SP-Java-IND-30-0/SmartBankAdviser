package com.star.bank.model.rule;
/**
 * Интерфейс для всех типов правил.
 * Определяет метод для получения подзапроса правила.
 */
public interface Rule {
    /**
     * Возвращает подзапрос для правила.
     *
     * @return Подзапрос в виде строки.
     */
    String getSubQuery();
}

