package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.Livraria;
import com.livraria.repository.LivrariaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação concreta do LivrariaRepository usando JDBC + SQLite.
 */
public class LivrariaRepositoryImpl implements LivrariaRepository {

    @Override
    public Livraria salvar(Livraria livraria) {
        String sql = "INSERT INTO livrarias (id_admin, nome, cidade) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, livraria.getIdAdmin());
            stmt.setString(2, livraria.getNome());
            stmt.setString(3, livraria.getCidade());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livraria.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar livraria: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar livraria", e);
        }

        return livraria;
    }

    @Override
    public Optional<Livraria> buscarPorId(int id) {
        String sql = "SELECT id, id_admin, nome, cidade FROM livrarias WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLivraria(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livraria por id: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Livraria> listarTodos() {
        String sql = "SELECT id, id_admin, nome, cidade FROM livrarias ORDER BY nome";
        List<Livraria> livrarias = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livrarias.add(mapearLivraria(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar livrarias: " + e.getMessage());
        }

        return livrarias;
    }

    @Override
    public boolean atualizar(Livraria livraria) {
        String sql = "UPDATE livrarias SET id_admin = ?, nome = ?, cidade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livraria.getIdAdmin());
            stmt.setString(2, livraria.getNome());
            stmt.setString(3, livraria.getCidade());
            stmt.setInt(4, livraria.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livraria: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM livrarias WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar livraria: " + e.getMessage());
            return false;
        }
    }

    private Livraria mapearLivraria(ResultSet rs) throws SQLException {
        return new Livraria(
                rs.getInt("id"),
                rs.getInt("id_admin"),
                rs.getString("nome"),
                rs.getString("cidade"));
    }
}
