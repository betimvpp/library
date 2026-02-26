package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.Autor;
import com.livraria.repository.AutorRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutorRepositoryImpl implements AutorRepository {

    @Override
    public Autor salvar(Autor autor) {
        String sql = "INSERT INTO autores (nome_completo, cpf, data_nascimento, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, autor.getNomeCompleto());
            stmt.setString(2, autor.getCpf());
            stmt.setString(3, autor.getDataNascimento().toString());
            if (autor.getEmail() != null)
                stmt.setString(4, autor.getEmail());
            else
                stmt.setNull(4, Types.VARCHAR);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    autor.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar autor", e);
        }

        return autor;
    }

    @Override
    public Optional<Autor> buscarPorId(int id) {
        String sql = "SELECT id, nome_completo, cpf, data_nascimento, email FROM autores WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar autor por id: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Autor> buscarPorCpf(String cpf) {
        String sql = "SELECT id, nome_completo, cpf, data_nascimento, email FROM autores WHERE cpf = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar autor por CPF: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Autor> listarTodos() {
        String sql = "SELECT id, nome_completo, cpf, data_nascimento, email FROM autores ORDER BY nome_completo";
        List<Autor> autores = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next())
                autores.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar autores: " + e.getMessage());
        }

        return autores;
    }

    @Override
    public boolean atualizar(Autor autor) {
        String sql = "UPDATE autores SET nome_completo=?, cpf=?, data_nascimento=?, email=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, autor.getNomeCompleto());
            stmt.setString(2, autor.getCpf());
            stmt.setString(3, autor.getDataNascimento().toString());
            if (autor.getEmail() != null)
                stmt.setString(4, autor.getEmail());
            else
                stmt.setNull(4, Types.VARCHAR);
            stmt.setInt(5, autor.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar autor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM autores WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar autor: " + e.getMessage());
            return false;
        }
    }

    private Autor mapear(ResultSet rs) throws SQLException {
        return new Autor(
                rs.getInt("id"),
                rs.getString("nome_completo"),
                rs.getString("cpf"),
                LocalDate.parse(rs.getString("data_nascimento")),
                rs.getString("email"));
    }
}
