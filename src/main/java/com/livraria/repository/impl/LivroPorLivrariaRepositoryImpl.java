package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.LivroPorLivraria;
import com.livraria.repository.LivroPorLivrariaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroPorLivrariaRepositoryImpl implements LivroPorLivrariaRepository {

    @Override
    public LivroPorLivraria salvar(LivroPorLivraria lpl) {
        String sql = "INSERT INTO livros_por_livraria (id_livraria, id_livro, estoque, estoque_disponivel) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lpl.getIdLivraria());
            stmt.setInt(2, lpl.getIdLivro());
            stmt.setInt(3, lpl.getEstoque());
            stmt.setInt(4, lpl.getEstoqueDisponivel());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    lpl.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar LivroPorLivraria: " + e.getMessage(), e);
        }

        return lpl;
    }

    @Override
    public Optional<LivroPorLivraria> buscarPorId(int id) {
        String sql = "SELECT id, id_livraria, id_livro, estoque, estoque_disponivel FROM livros_por_livraria WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar LivroPorLivraria: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<LivroPorLivraria> listarPorLivraria(int idLivraria) {
        String sql = "SELECT id, id_livraria, id_livro, estoque, estoque_disponivel FROM livros_por_livraria WHERE id_livraria = ?";
        List<LivroPorLivraria> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivraria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar LivrosPorLivraria: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<LivroPorLivraria> listarDisponiveis(int idLivraria) {
        String sql = "SELECT id, id_livraria, id_livro, estoque, estoque_disponivel FROM livros_por_livraria WHERE id_livraria = ? AND estoque_disponivel > 0";
        List<LivroPorLivraria> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivraria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar livros disponíveis: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean atualizar(LivroPorLivraria lpl) {
        String sql = "UPDATE livros_por_livraria SET estoque = ?, estoque_disponivel = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lpl.getEstoque());
            stmt.setInt(2, lpl.getEstoqueDisponivel());
            stmt.setInt(3, lpl.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar LivroPorLivraria: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM livros_por_livraria WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar LivroPorLivraria: " + e.getMessage());
            return false;
        }
    }

    private LivroPorLivraria mapear(ResultSet rs) throws SQLException {
        return new LivroPorLivraria(
                rs.getInt("id"),
                rs.getInt("id_livraria"),
                rs.getInt("id_livro"),
                rs.getInt("estoque"),
                rs.getInt("estoque_disponivel"));
    }
}
