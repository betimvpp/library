package com.livraria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão com o banco de dados SQLite.
 * Implementa o padrão Singleton para garantir uma única conexão ativa.
 */
public class DatabaseConnection {

    // O arquivo .db ficará em src/main/resources/
    private static final String URL = "jdbc:sqlite:src/main/resources/livraria.db";
    private static Connection connection;

    private DatabaseConnection() {
    }

    /**
     * Retorna a conexão ativa com o banco de dados.
     * Cria uma nova conexão se ainda não existir.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            System.out.println("Conexão com SQLite estabelecida!");
        }
        return connection;
    }

    /**
     * Fecha a conexão com o banco de dados.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão com SQLite encerrada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
