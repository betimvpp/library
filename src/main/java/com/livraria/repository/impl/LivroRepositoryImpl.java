package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.Livro;
import com.livraria.repository.LivroRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação concreta do LivroRepository usando JDBC + SQLite.
 */
public class LivroRepositoryImpl implements LivroRepository {

    @Override
    public Livro salvar(Livro livro) {
        String sql = "INSERT INTO livros (id_autor, titulo, data_lancamento, data_atualizacao) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, livro.getIdAutor());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getDataLancamento().toString());
            // data_atualizacao pode ser null
            if (livro.getDataAtualizacao() != null) {
                stmt.setString(4, livro.getDataAtualizacao().toString());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livro.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar livro: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar livro", e);
        }

        return livro;
    }

    @Override
    public Optional<Livro> buscarPorId(int id) {
        String sql = "SELECT id, id_autor, titulo, data_lancamento, data_atualizacao FROM livros WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por id: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Livro> listarPorAutor(int idAutor) {
        String sql = "SELECT id, id_autor, titulo, data_lancamento, data_atualizacao FROM livros WHERE id_autor = ? ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAutor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar livros por autor: " + e.getMessage());
        }

        return livros;
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        String sql = "SELECT id, id_autor, titulo, data_lancamento, data_atualizacao FROM livros WHERE titulo LIKE ? ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por título: " + e.getMessage());
        }

        return livros;
    }

    @Override
    public List<Livro> buscarPorNomeAutor(String nomeAutor) {
        String sql = """
                SELECT l.id, l.id_autor, l.titulo, l.data_lancamento, l.data_atualizacao
                FROM livros l
                JOIN autores a ON l.id_autor = a.id
                WHERE a.nome_completo LIKE ?
                ORDER BY l.titulo
                """;
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeAutor + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por autor: " + e.getMessage());
        }

        return livros;
    }

    @Override
    public List<Livro> listarTodos() {
        String sql = "SELECT id, id_autor, titulo, data_lancamento, data_atualizacao FROM livros ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next())
                livros.add(mapearLivro(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }

        return livros;
    }

    @Override
    public boolean atualizar(Livro livro) {
        String sql = "UPDATE livros SET id_autor = ?, titulo = ?, data_lancamento = ?, data_atualizacao = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livro.getIdAutor());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getDataLancamento().toString());
            if (livro.getDataAtualizacao() != null) {
                stmt.setString(4, livro.getDataAtualizacao().toString());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.setInt(5, livro.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar livro: " + e.getMessage());
            return false;
        }
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        String dataAtualizacaoStr = rs.getString("data_atualizacao");
        LocalDate dataAtualizacao = dataAtualizacaoStr != null ? LocalDate.parse(dataAtualizacaoStr) : null;

        return new Livro(
                rs.getInt("id"),
                rs.getInt("id_autor"),
                rs.getString("titulo"),
                LocalDate.parse(rs.getString("data_lancamento")),
                dataAtualizacao);
    }
}
