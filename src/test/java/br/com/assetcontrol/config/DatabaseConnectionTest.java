package br.com.assetcontrol.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            DatabaseMetaData metadata =
                    connection.getMetaData();

            System.out.println(
                    "Conexão com PostgreSQL estabelecida!"
            );

            System.out.println(
                    "Banco: " + metadata.getDatabaseProductName()
            );

            System.out.println(
                    "Versão: " + metadata.getDatabaseProductVersion()
            );

            System.out.println(
                    "Conexão válida: " + connection.isValid(5)
            );

        } catch (SQLException | IllegalStateException e) {
            System.err.println(
                    "Não foi possível conectar ao PostgreSQL."
            );
            System.err.println(e.getMessage());
        }
    }
}
