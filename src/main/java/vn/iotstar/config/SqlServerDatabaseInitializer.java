package vn.iotstar.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public final class SqlServerDatabaseInitializer {
    private SqlServerDatabaseInitializer() {
    }

    public static void createIfMissing(String jdbcUrl, String username, String password, String databaseName)
            throws SQLException {
        validateDatabaseName(databaseName);
        String masterUrl = toMasterDatabaseUrl(jdbcUrl);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("Microsoft SQL Server JDBC driver is missing from the application.", exception);
        }

        try (Connection connection = DriverManager.getConnection(masterUrl, username, password)) {
            if (databaseExists(connection, databaseName)) {
                return;
            }

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE " + quoteIdentifier(databaseName));
            } catch (SQLException exception) {
                if (!databaseExists(connection, databaseName)) {
                    throw exception;
                }
            }
        }
    }

    static String toMasterDatabaseUrl(String jdbcUrl) {
        return withDatabase(jdbcUrl, "master");
    }

    static String toDatabaseUrl(String jdbcUrl, String databaseName) {
        validateDatabaseName(databaseName);
        return withDatabase(jdbcUrl, databaseName);
    }

    private static String withDatabase(String jdbcUrl, String databaseName) {
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:sqlserver://")) {
            throw new IllegalArgumentException("DB_URL must be a SQL Server JDBC URL.");
        }
        String withoutDatabase = jdbcUrl.replaceAll("(?i);\\s*database(Name)?\\s*=\\s*[^;]*", "");
        return withoutDatabase + ";databaseName=" + databaseName;
    }

    static String quoteIdentifier(String identifier) {
        return "[" + identifier.replace("]", "]]") + "]";
    }

    private static boolean databaseExists(Connection connection, String databaseName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT DB_ID(?)")) {
            statement.setString(1, databaseName);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() && result.getObject(1) != null;
            }
        }
    }

    private static void validateDatabaseName(String databaseName) {
        if (databaseName == null || !databaseName.matches("[A-Za-z0-9_-]{1,128}")) {
            throw new IllegalArgumentException(
                    "DB_NAME must contain 1-128 letters, digits, underscores or hyphens.");
        }
        String normalized = databaseName.toLowerCase(Locale.ROOT);
        if (normalized.equals("master") || normalized.equals("model")
                || normalized.equals("msdb") || normalized.equals("tempdb")) {
            throw new IllegalArgumentException("DB_NAME cannot be a SQL Server system database.");
        }
    }
}
