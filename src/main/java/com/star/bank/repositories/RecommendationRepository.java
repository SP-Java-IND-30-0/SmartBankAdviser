package com.star.bank.repositories;

import com.star.bank.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("h2JdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value = "userProductRulesCache", key = "#userId + '_' + #query")
    public boolean checkProductRules(String userId, String query) {
        return Boolean.TRUE.equals(jdbcTemplate.query(query, rs -> rs.next() && rs.getBoolean(1), userId));
    }


    public boolean isUserExist(String userId) {
        return Boolean.TRUE.equals(
                jdbcTemplate
                        .query("SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)", rs -> rs.next()
                                && rs.getBoolean(1), userId));
    }

    public List<UserDto> getUser(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                (rs, rowNum) -> UserDto.builder()
                        .id(rs.getString("id"))
                        .username("username")
                        .lastName("last_name")
                        .firstName("first_name")
                        .build(),
                username);
    }
}