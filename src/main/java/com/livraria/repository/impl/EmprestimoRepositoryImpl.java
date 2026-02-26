package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.Emprestimo;
import com.livraria.repository.EmprestimoRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmprestimoRepositoryImpl implements EmprestimoRepository {

    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (id_livraria, id_livro_por_livraria, id_consumidor, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emprestimo.getIdLivraria());
            stmt.setInt(2, emprestimo.getIdLivroPorLivraria());
            stmt.setInt(3, emprestimo.getIdConsumidor());
            stmt.setString(4, emprestimo.getDataEmprestimo().toString());
            stmt.setNull(5, Types.VARCHAR); // data_devolucao null no empréstimo inicial
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    emprestimo.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar empréstimo: " + e.getMessage(), e);
        }

        return emprestimo;
    }

    @Override
    public Optional<Emprestimo> buscarPorId(int id) {
        String sql = "SELECT id, id_livraria, id_livro_por_livraria, id_consumidor, data_emprestimo, data_devolucao FROM emprestimos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimo: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Emprestimo> listarPorConsumidor(int idConsumidor) {
        String sql = "SELECT id, id_livraria, id_livro_por_livraria, id_consumidor, data_emprestimo, data_devolucao FROM emprestimos WHERE id_consumidor = ? ORDER BY data_emprestimo DESC";
        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConsumidor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos por consumidor: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Emprestimo> listarAtivos(int idLivroPorLivraria) {
        String sql = "SELECT id, id_livraria, id_livro_por_livraria, id_consumidor, data_emprestimo, data_devolucao FROM emprestimos WHERE id_livro_por_livraria = ? AND data_devolucao IS NULL";
        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivroPorLivraria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos ativos: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean atualizar(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimos SET data_devolucao = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (emprestimo.getDataDevolucao() != null) {
                stmt.setString(1, emprestimo.getDataDevolucao().toString());
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }
            stmt.setInt(2, emprestimo.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
            return false;
        }
    }

    private Emprestimo mapear(ResultSet rs) throws SQLException {
        String devStr = rs.getString("data_devolucao");
        return new Emprestimo(
                rs.getInt("id"),
                rs.getInt("id_livraria"),
                rs.getInt("id_livro_por_livraria"),
                rs.getInt("id_consumidor"),
                LocalDate.parse(rs.getString("data_emprestimo")),
                devStr != null ? LocalDate.parse(devStr) : null);
    }
}
