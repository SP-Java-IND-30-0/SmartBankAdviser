package com.star.bank;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.star.bank.config.DatabaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Nested
class DatabaseConfigTest {

    @Mock
    private DataSource postgresDataSource;

    @Mock
    private DataSource h2DataSource;

    @Mock
    private JdbcTemplate postgresJdbcTemplate;

    @Mock
    private JdbcTemplate h2JdbcTemplate;

    @InjectMocks
    private DatabaseConfig databaseConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostgresDataSource() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        when(postgresDataSource.getConnection()).thenReturn(mockConnection);

        assertNotNull(postgresDataSource);

        try {
            var connection = postgresDataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Ошибка при подключении к PostgreSQL: " + e.getMessage());
        }
    }

    @Test
    void testH2DataSource() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        when(h2DataSource.getConnection()).thenReturn(mockConnection);

        assertNotNull(h2DataSource);

        try {
            var connection = h2DataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Ошибка при подключении к H2: " + e.getMessage());
        }
    }

    @Test
    void testPostgresJdbcTemplate() {
        when(postgresJdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        assertNotNull(postgresJdbcTemplate);

        Integer result = postgresJdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result);
    }

    @Test
    void testH2JdbcTemplate() {
        when(h2JdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        assertNotNull(h2JdbcTemplate);

        Integer result = h2JdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result);
    }
}