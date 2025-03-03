package com.star.bank.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO для пользователя.
 * Содержит идентификатор, имя пользователя, имя и фамилию.
 */
@Data
@Builder
public class UserDto {
    /**
     * Идентификатор пользователя.
     */
    String id;

    /**
     * username пользователя.
     */
    String username;

    /**
     * Имя пользователя.
     */
    String firstName;

    /**
     * Фамилия пользователя.
     */
    String lastName;
}
