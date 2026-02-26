package com.livraria.service;

import com.livraria.model.Livro;
import com.livraria.repository.AutorRepository;
import com.livraria.repository.LivroRepository;
import com.livraria.repository.impl.AutorRepositoryImpl;
import com.livraria.repository.impl.LivroRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço para a entidade Livro.
 * Garante que somente um Autor pode publicar e atualizar um Livro.
 */
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public LivroService() {
        this.livroRepository = new LivroRepositoryImpl();
        this.autorRepository = new AutorRepositoryImpl();
    }

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    /**
     * Publica um novo livro vinculado a um Autor existente.
     *
     * @throws IllegalArgumentException se o título estiver em branco, a data for
     *                                  inválida
     *                                  ou o idAutor não corresponder a nenhum Autor
     */
    public Livro publicarLivro(int idAutor, String titulo, LocalDate dataLancamento) {
        validarAutor(idAutor);
        validarTitulo(titulo);
        validarDataLancamento(dataLancamento);

        Livro livro = new Livro(idAutor, titulo, dataLancamento);
        return livroRepository.salvar(livro);
    }

    /**
     * Atualiza o título de um livro existente.
     * Somente o Autor original do livro pode realizar a atualização.
     * A dataAtualizacao é preenchida automaticamente com a data atual.
     *
     * @throws IllegalArgumentException se o livro não existir ou o Autor não for o
     *                                  dono
     */
    public Livro atualizarLivro(int idLivro, int idAutor, String novoTitulo) {
        validarTitulo(novoTitulo);

        Livro livro = livroRepository.buscarPorId(idLivro)
                .orElseThrow(() -> new IllegalArgumentException("Livro com id " + idLivro + " não encontrado."));

        if (livro.getIdAutor() != idAutor) {
            throw new IllegalArgumentException(
                    "Somente o Autor original pode atualizar este livro.");
        }

        livro.setTitulo(novoTitulo);
        livro.setDataAtualizacao(LocalDate.now());
        livroRepository.atualizar(livro);

        return livro;
    }

    public Optional<Livro> buscarPorId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("O id deve ser positivo.");
        return livroRepository.buscarPorId(id);
    }

    public List<Livro> listarPorAutor(int idAutor) {
        validarAutor(idAutor);
        return livroRepository.listarPorAutor(idAutor);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("Informe um termo para busca.");
        return livroRepository.buscarPorTitulo(titulo);
    }

    public List<Livro> buscarPorNomeAutor(String nomeAutor) {
        if (nomeAutor == null || nomeAutor.isBlank())
            throw new IllegalArgumentException("Informe o nome do autor.");
        return livroRepository.buscarPorNomeAutor(nomeAutor);
    }

    public List<Livro> listarTodos() {
        return livroRepository.listarTodos();
    }

    public boolean deletarLivro(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("O id deve ser positivo.");
        return livroRepository.deletar(id);
    }

    // --- Validações privadas ---

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("O título do livro é obrigatório.");
    }

    private void validarDataLancamento(LocalDate data) {
        if (data == null)
            throw new IllegalArgumentException("A data de lançamento é obrigatória.");
    }

    private void validarAutor(int idAutor) {
        autorRepository.buscarPorId(idAutor)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Autor com id " + idAutor + " não encontrado. " +
                                "Somente um Autor cadastrado pode publicar livros."));
    }
}
