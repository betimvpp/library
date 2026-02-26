package com.livraria.service;

import com.livraria.model.LivroPorLivraria;
import com.livraria.repository.LivroPorLivrariaRepository;
import com.livraria.repository.impl.LivroPorLivrariaRepositoryImpl;

import java.util.List;
import java.util.Optional;

/**
 * Gerencia a adição de livros ao acervo de uma livraria e o controle de
 * estoque.
 */
public class LivroPorLivrariaService {

    private final LivroPorLivrariaRepository repository;

    public LivroPorLivrariaService() {
        this.repository = new LivroPorLivrariaRepositoryImpl();
    }

    public LivroPorLivrariaService(LivroPorLivrariaRepository repository) {
        this.repository = repository;
    }

    /**
     * Adiciona um livro ao acervo de uma livraria com uma quantidade inicial de
     * estoque.
     */
    public LivroPorLivraria adicionarAcervo(int idLivraria, int idLivro, int estoque) {
        if (estoque <= 0)
            throw new IllegalArgumentException("O estoque inicial deve ser maior que zero.");
        LivroPorLivraria lpl = new LivroPorLivraria(idLivraria, idLivro, estoque);
        return repository.salvar(lpl);
    }

    public Optional<LivroPorLivraria> buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<LivroPorLivraria> listarPorLivraria(int idLivraria) {
        return repository.listarPorLivraria(idLivraria);
    }

    public List<LivroPorLivraria> listarDisponiveis(int idLivraria) {
        return repository.listarDisponiveis(idLivraria);
    }

    /**
     * Reduz o estoque total e disponível (venda permanente).
     */
    public void decrementarEstoqueVenda(LivroPorLivraria lpl) {
        lpl.setEstoque(lpl.getEstoque() - 1);
        lpl.setEstoqueDisponivel(lpl.getEstoqueDisponivel() - 1);
        repository.atualizar(lpl);
    }

    /**
     * Reduz apenas o estoque disponível (empréstimo temporário).
     */
    public void decrementarEstoqueEmprestimo(LivroPorLivraria lpl) {
        lpl.setEstoqueDisponivel(lpl.getEstoqueDisponivel() - 1);
        repository.atualizar(lpl);
    }

    /**
     * Devolve uma unidade ao estoque disponível após devolução de empréstimo.
     */
    public void incrementarEstoqueDevolucao(LivroPorLivraria lpl) {
        lpl.setEstoqueDisponivel(lpl.getEstoqueDisponivel() + 1);
        repository.atualizar(lpl);
    }
}
