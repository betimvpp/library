package com.livraria.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializar() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id               INTEGER PRIMARY KEY AUTOINCREMENT,
                        cpf              TEXT    NOT NULL UNIQUE,
                        nome_completo    TEXT    NOT NULL,
                        data_nascimento  TEXT    NOT NULL,
                        role             TEXT    NOT NULL CHECK(role IN ('ADMIN', 'CONSUMIDOR')),
                        email            TEXT
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS autores (
                        id               INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome_completo    TEXT    NOT NULL,
                        cpf              TEXT    NOT NULL UNIQUE,
                        data_nascimento  TEXT    NOT NULL,
                        email            TEXT
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS livrarias (
                        id       INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_admin INTEGER NOT NULL REFERENCES usuarios(id),
                        nome     TEXT    NOT NULL,
                        cidade   TEXT    NOT NULL
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS livros (
                        id               INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_autor         INTEGER NOT NULL REFERENCES autores(id),
                        titulo           TEXT    NOT NULL,
                        data_lancamento  TEXT    NOT NULL,
                        data_atualizacao TEXT
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS livros_por_livraria (
                        id                  INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_livraria         INTEGER NOT NULL REFERENCES livrarias(id),
                        id_livro            INTEGER NOT NULL REFERENCES livros(id),
                        estoque             INTEGER NOT NULL DEFAULT 0,
                        estoque_disponivel  INTEGER NOT NULL DEFAULT 0
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS vendas (
                        id                    INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_livraria           INTEGER NOT NULL REFERENCES livrarias(id),
                        id_livro_por_livraria INTEGER NOT NULL REFERENCES livros_por_livraria(id),
                        id_consumidor         INTEGER NOT NULL REFERENCES usuarios(id),
                        data_venda            TEXT    NOT NULL
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS emprestimos (
                        id                    INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_livraria           INTEGER NOT NULL REFERENCES livrarias(id),
                        id_livro_por_livraria INTEGER NOT NULL REFERENCES livros_por_livraria(id),
                        id_consumidor         INTEGER NOT NULL REFERENCES usuarios(id),
                        data_emprestimo       TEXT    NOT NULL,
                        data_devolucao        TEXT
                    );
                    """);

            System.out.println("Banco de dados inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco de dados: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do banco de dados", e);
        }
    }
}
