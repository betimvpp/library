package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.Venda;
import com.livraria.repository.VendaRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendaRepositoryImpl implements VendaRepository {

    @Override
    public Venda salvar(Venda venda) {
        String sql = "INSERT INTO vendas (id_livraria, id_livro_por_livraria, id_consumidor, data_venda) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, venda.getIdLivraria());
            stmt.setInt(2, venda.getIdLivroPorLivraria());
            stmt.setInt(3, venda.getIdConsumidor());
            stmt.setString(4, venda.getDataVenda().toString());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    venda.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar venda: " + e.getMessage(), e);
        }

        return venda;
    }

    @Override
    public Optional<Venda> buscarPorId(int id) {
        String sql = "SELECT id, id_livraria, id_livro_por_livraria, id_consumidor, data_venda FROM vendas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Venda> listarPorLivraria(int idLivraria) {
        String sql = "SELECT id, id_livraria, id_livro_por_livraria, id_consumidor, data_venda FROM vendas WHERE id_livraria = ? ORDER BY data_venda DESC";
        List<Venda> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivraria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas por livraria: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Venda> listarPorConsumidor(int idConsumidor) {
        String sql = "SELECT id, id_livraria, id_livro_por_livraria, id_consumidor, data_venda FROM vendas WHERE id_consumidor = ? ORDER BY data_venda DESC";
        List<Venda> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConsumidor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas por consumidor: " + e.getMessage());
        }

        return lista;
    }

    private Venda mapear(ResultSet rs) throws SQLException {
        return new Venda(
                rs.getInt("id"),
                rs.getInt("id_livraria"),
                rs.getInt("id_livro_por_livraria"),
                rs.getInt("id_consumidor"),
                LocalDate.parse(rs.getString("data_venda")));
    }
}
