package com.livraria.service;

import com.livraria.exception.AutorCpfJaCadastradoException;
import com.livraria.model.Autor;
import com.livraria.repository.AutorRepository;
import com.livraria.repository.impl.AutorRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService() {
        this.autorRepository = new AutorRepositoryImpl();
    }

    public AutorService(AutorRepository r) {
        this.autorRepository = r;
    }

    public Autor cadastrarAutor(String nomeCompleto, String cpf, LocalDate dataNascimento) {
        return cadastrarAutor(nomeCompleto, cpf, dataNascimento, null);
    }

    public Autor cadastrarAutor(String nomeCompleto, String cpf, LocalDate dataNascimento, String email) {
        validarNome(nomeCompleto);
        validarCpf(cpf);
        validarDataNascimento(dataNascimento);

        String cpfNumeros = cpf.replaceAll("[.\\-]", "");

        autorRepository.buscarPorCpf(cpfNumeros).ifPresent(e -> {
            throw new AutorCpfJaCadastradoException(cpfNumeros);
        });

        return autorRepository.salvar(new Autor(nomeCompleto, cpfNumeros, dataNascimento, email));
    }

    public Optional<Autor> buscarPorId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Id inválido.");
        return autorRepository.buscarPorId(id);
    }

    public Optional<Autor> buscarPorCpf(String cpf) {
        validarCpf(cpf);
        return autorRepository.buscarPorCpf(cpf.replaceAll("[.\\-]", ""));
    }

    public List<Autor> listarTodos() {
        return autorRepository.listarTodos();
    }

    public boolean atualizarAutor(Autor autor) {
        if (autor.getId() <= 0)
            throw new IllegalArgumentException("Id inválido.");
        return autorRepository.atualizar(autor);
    }

    public boolean deletarAutor(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Id inválido.");
        return autorRepository.deletar(id);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome obrigatório.");
        if (nome.trim().length() < 3)
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres.");
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank())
            throw new IllegalArgumentException("CPF obrigatório.");
        if (!cpf.replaceAll("[.\\-]", "").matches("\\d{11}"))
            throw new IllegalArgumentException("CPF deve ter 11 dígitos numéricos.");
    }

    private void validarDataNascimento(LocalDate data) {
        if (data == null)
            throw new IllegalArgumentException("Data de nascimento obrigatória.");
        if (data.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data não pode ser no futuro.");
    }
}
