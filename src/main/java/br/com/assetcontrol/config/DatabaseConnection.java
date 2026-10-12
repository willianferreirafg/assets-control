package br.com.assetcontrol.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private DatabaseConnection() {
        // impede a instanciação desta classe utilitária.
    }

    public static Connection getConnection() throws SQLException {
        String url = requireEnvironmentVariable(
                "ASSETCONTROL_DB_URL"
        );

        String user = requireEnvironmentVariable(
                "ASSETCONTROL_DB_USER"
        );

        String password = requireEnvironmentVariable(
                "ASSETCONTROL_DB_PASSWORD"
        );

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver JDBC do PostgreSQL não encontrado. "
                            + "Verifique o classpath da aplicação.",
                    e
            );
        }

        return DriverManager.getConnection(url, user, password);
    }

    private static String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "A variável de ambiente " + name
                            + " não foi configurada."
            );
        }

        return value;
    }

}
