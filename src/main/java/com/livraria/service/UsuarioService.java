package com.livraria.service;

import com.livraria.exception.CpfJaCadastradoException;
import com.livraria.model.Admin;
import com.livraria.model.Consumidor;
import com.livraria.model.Usuario;
import com.livraria.model.enums.Role;
import com.livraria.repository.UsuarioRepository;
import com.livraria.repository.impl.UsuarioRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    public UsuarioService(UsuarioRepository r) {
        this.usuarioRepository = r;
    }

    public Usuario cadastrarUsuario(String cpf, String nomeCompleto, LocalDate dataNascimento, Role role) {
        return cadastrarUsuario(cpf, nomeCompleto, dataNascimento, role, null);
    }

    public Usuario cadastrarUsuario(String cpf, String nomeCompleto, LocalDate dataNascimento, Role role,
            String email) {
        validarCpf(cpf);
        validarNome(nomeCompleto);
        validarDataNascimento(dataNascimento);

        String cpfNumeros = cpf.replaceAll("[.\\-]", "");

        usuarioRepository.buscarPorCpf(cpfNumeros).ifPresent(e -> {
            throw new CpfJaCadastradoException(cpfNumeros);
        });

        Usuario usuario = switch (role) {
            case ADMIN -> new Admin(cpfNumeros, nomeCompleto, dataNascimento, email);
            case CONSUMIDOR -> new Consumidor(cpfNumeros, nomeCompleto, dataNascimento, email);
        };

        return usuarioRepository.salvar(usuario);
    }

    public Optional<Usuario> buscarPorId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("O id deve ser positivo.");
        return usuarioRepository.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorCpf(String cpf) {
        validarCpf(cpf);
        return usuarioRepository.buscarPorCpf(cpf.replaceAll("[.\\-]", ""));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    public boolean atualizarUsuario(Usuario usuario) {
        if (usuario.getId() <= 0)
            throw new IllegalArgumentException("Id inválido.");
        return usuarioRepository.atualizar(usuario);
    }

    public boolean deletarUsuario(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Id inválido.");
        return usuarioRepository.deletar(id);
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank())
            throw new IllegalArgumentException("CPF obrigatório.");
        if (!cpf.replaceAll("[.\\-]", "").matches("\\d{11}"))
            throw new IllegalArgumentException("CPF deve ter 11 dígitos numéricos.");
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome obrigatório.");
        if (nome.trim().length() < 3)
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres.");
    }

    private void validarDataNascimento(LocalDate data) {
        if (data == null)
            throw new IllegalArgumentException("Data de nascimento obrigatória.");
        if (data.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data não pode ser no futuro.");
    }
}
