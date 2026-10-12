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
