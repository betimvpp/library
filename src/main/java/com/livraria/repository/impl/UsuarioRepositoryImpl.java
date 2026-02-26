package com.livraria.repository.impl;

import com.livraria.database.DatabaseConnection;
import com.livraria.model.Admin;
import com.livraria.model.Consumidor;
import com.livraria.model.Usuario;
import com.livraria.model.enums.Role;
import com.livraria.repository.UsuarioRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (cpf, nome_completo, data_nascimento, role, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNomeCompleto());
            stmt.setString(3, usuario.getDataNascimento().toString());
            stmt.setString(4, usuario.getRole().name());
            if (usuario.getEmail() != null)
                stmt.setString(5, usuario.getEmail());
            else
                stmt.setNull(5, Types.VARCHAR);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    usuario.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }

        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT id, cpf, nome_completo, data_nascimento, role, email FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por id: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        String sql = "SELECT id, cpf, nome_completo, data_nascimento, role, email FROM usuarios WHERE cpf = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por CPF: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT id, cpf, nome_completo, data_nascimento, role, email FROM usuarios ORDER BY nome_completo";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next())
                usuarios.add(mapear(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    @Override
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET cpf=?, nome_completo=?, data_nascimento=?, role=?, email=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNomeCompleto());
            stmt.setString(3, usuario.getDataNascimento().toString());
            stmt.setString(4, usuario.getRole().name());
            if (usuario.getEmail() != null)
                stmt.setString(5, usuario.getEmail());
            else
                stmt.setNull(5, Types.VARCHAR);
            stmt.setInt(6, usuario.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            return false;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String cpf = rs.getString("cpf");
        String nome = rs.getString("nome_completo");
        LocalDate dataNasc = LocalDate.parse(rs.getString("data_nascimento"));
        Role role = Role.valueOf(rs.getString("role"));
        String email = rs.getString("email");

        return switch (role) {
            case ADMIN -> new Admin(id, cpf, nome, dataNasc, email);
            case CONSUMIDOR -> new Consumidor(id, cpf, nome, dataNasc, email);
        };
    }
}
