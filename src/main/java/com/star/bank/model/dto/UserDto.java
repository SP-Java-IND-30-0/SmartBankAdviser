package com.star.bank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    String id;
    String username;
    String firstName;
    String lastName;
}
