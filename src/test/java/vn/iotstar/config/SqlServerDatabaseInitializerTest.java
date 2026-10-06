package vn.iotstar.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SqlServerDatabaseInitializerTest {
    @Test
    void replacesDatabaseNameInSqlServerUrl() {
        assertEquals(
                "jdbc:sqlserver://localhost:1433;encrypt=true;databaseName=master",
                SqlServerDatabaseInitializer.toMasterDatabaseUrl(
                        "jdbc:sqlserver://localhost:1433;databaseName=Bai4DB;encrypt=true"));
    }

    @Test
    void addsDatabaseNameWhenUrlDoesNotSpecifyOne() {
        assertEquals(
                "jdbc:sqlserver://localhost:1433;encrypt=true;databaseName=Bai4DB",
                SqlServerDatabaseInitializer.toDatabaseUrl(
                        "jdbc:sqlserver://localhost:1433;encrypt=true", "Bai4DB"));
    }

    @Test
    void rejectsDatabaseNamesThatCouldAlterTheJdbcUrl() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SqlServerDatabaseInitializer.toDatabaseUrl(
                        "jdbc:sqlserver://localhost:1433", "Bai4DB;encrypt=false"));
    }
}
